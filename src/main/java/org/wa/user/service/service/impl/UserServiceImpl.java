package org.wa.user.service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wa.user.service.config.UserAccessService;
import org.wa.user.service.dto.common.PageResponse;
import org.wa.user.service.dto.user.UserCreateDto;
import org.wa.user.service.dto.user.UserResponseDto;
import org.wa.user.service.dto.user.UserShortInfoDto;
import org.wa.user.service.dto.user.UserUpdateDto;
import org.wa.user.service.dto.user.UserStatusUpdateDto;
import org.wa.user.service.exception.AttributeDuplicateException;
import org.wa.user.service.exception.ResourceNotFoundException;
import org.wa.user.service.mapper.UserMapper;
import org.wa.user.service.entity.UserEntity;
import org.wa.user.service.entity.enumeration.Status;
import org.wa.user.service.repository.UserRepository;
import org.wa.user.service.service.UserService;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserAccessService accessService;

    @Override
    public PageResponse<UserShortInfoDto> getAllUsers(Pageable pageable) {
        log.info("Getting all users with pagination - page: {}, size: {}, sort: {}",
                pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());

        Page<UserEntity> users = userRepository.findAll(pageable);
        List<UserShortInfoDto> content = users.getContent().stream()
                .map(userMapper::toShortInfoDto).toList();

        log.info("Successfully retrieved {} users out of {} total", content.size(), users.getTotalElements());

        return PageResponse.of(users, content);
    }

    @Override
    public PageResponse<UserShortInfoDto> getNonDeletedUsers(Pageable pageable) {
        log.info("Getting active users with pagination - page: {}, size: {}",
                pageable.getPageNumber(), pageable.getPageSize());

        Page<UserEntity> users = userRepository.findByStatusNot(Status.DELETED, pageable);
        List<UserShortInfoDto> content = users.getContent().stream()
                .map(userMapper::toShortInfoDto).toList();

        log.info("Successfully retrieved {} active users", content.size());

        return PageResponse.of(users, content);
    }

    @Override
    public UserResponseDto getUserById(Long userId) {
        log.info("Getting user by id: {}", userId);

        if (!accessService.isAdmin()) {
            accessService.checkUserAccess(userId);
        }

        UserEntity userEntity = getUserEntity(userId);

        if (userEntity.getStatus() == Status.DELETED) {
            log.warn("Tried to access deleted user with id: {}", userId);
            throw new ResourceNotFoundException("User not found with userId " + userId);
        }

        UserResponseDto response = userMapper.toResponseDto(userEntity);
        log.info("Successfully retrieved user with id: {}, email: {}", userId, userEntity.getEmail());

        return response;
    }

    @Override
    @Transactional
    public UserResponseDto createUser(UserCreateDto createDto) {
        log.info("Creating new user with email: {}", createDto.getEmail());

        if (userRepository.existsByEmail(createDto.getEmail())) {
            log.warn("Attempt to create user with duplicate email: {}", createDto.getEmail());
            throw new AttributeDuplicateException("Email is already exists: " + createDto.getEmail());
        }
        if (userRepository.existsByPhone(createDto.getPhone())) {
            log.warn("Attempt to create user with duplicate phone: {}", createDto.getPhone());
            throw new AttributeDuplicateException("Phone is already exists: " + createDto.getPhone());
        }

        log.debug("Mapping UserCreateDto to UserEntity");
        UserEntity userEntity = userMapper.toEntity(createDto);
        userEntity.setStatus(Status.ACTIVE);

        log.debug("Saving user to database");
        UserEntity savedUserEntity = userRepository.save(userEntity);
        log.info("Successfully created user with id: {} and email: {}",
                savedUserEntity.getId(), savedUserEntity.getEmail());

        UserResponseDto response = userMapper.toResponseDto(savedUserEntity);
        log.debug("Successfully mapped UserEntity to UserResponseDto");

        return response;
    }

    @Override
    @Transactional
    public UserResponseDto updateUser(Long userId, UserUpdateDto updateDto) {
        log.info("Updating user with id: {}", userId);

        if (!accessService.isAdmin()) {
            accessService.checkUserAccess(userId);
        }

        UserEntity userEntity = getUserEntity(userId);

        if (userEntity.getStatus() == Status.DELETED) {
            log.warn("Attempt to update deleted user with id: {}", userId);
            throw new ResourceNotFoundException("Cannot update deleted user with userId: " + userId);
        }

        if (updateDto.getEmail() != null &&
                !userEntity.getEmail().equals(updateDto.getEmail()) &&
                userRepository.existsByEmailAndIdNot(updateDto.getEmail(), userId)) {
            log.warn("Attempt to update user id: {} with duplicate email: {}", userId, updateDto.getEmail());
            throw new AttributeDuplicateException("Email already exists: " + updateDto.getEmail());
        }

        if (updateDto.getPhone() != null &&
                !userEntity.getPhone().equals(updateDto.getPhone()) &&
                userRepository.existsByPhoneAndIdNot(updateDto.getPhone(), userId)) {
            log.warn("Attempt to update user id: {} with duplicate phone: {}", userId, updateDto.getPhone());
            throw new AttributeDuplicateException("Phone already exists: " + updateDto.getPhone());
        }

        log.debug("Mapping UserUpdateDto to existing UserEntity");
        userMapper.updateEntityFromDto(updateDto, userEntity);

        log.debug("Saving updated user to database");
        UserEntity updatedUserEntity = userRepository.save(userEntity);

        UserResponseDto response = userMapper.toResponseDto(updatedUserEntity);
        log.info("Successfully updated user with id: {}", userId);

        return response;
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        log.info("Deleting user with id: {}", userId);

        if (!accessService.isAdmin()) {
            accessService.checkUserAccess(userId);
        }

        UserEntity userEntity = getUserEntity(userId);
        userEntity.setStatus(Status.DELETED);

        userRepository.save(userEntity);
        log.info("Successfully deleted user with id: {}", userId);
    }

    @Override
    @Transactional
    public void hardDeleteUser(Long userId) {
        log.info("Hard deleting user with id: {}", userId);

        if (!accessService.isAdmin()) {
            accessService.checkUserAccess(userId);
        }

        if (!userRepository.existsById(userId)) {
            log.warn("Attempt to hard delete non-existent user with id: {}", userId);
            throw new ResourceNotFoundException("User not found with this userId" + userId);
        }

        userRepository.deleteById(userId);
        log.info("Successfully hard deleted user with id: {}", userId);
    }

    @Override
    @Transactional
    public UserResponseDto updateUserStatus(Long userId, UserStatusUpdateDto updateDto) {
        log.info("Updating status for user id: {} to {}", userId, updateDto.getStatus());

        if (!accessService.isAdmin()) {
            accessService.checkUserAccess(userId);
        }

        UserEntity userEntity = getUserEntity(userId);
        userEntity.setStatus(updateDto.getStatus());

        UserEntity updatedUserEntity = userRepository.save(userEntity);
        log.info("Successfully updated status for user id: {} to {}", userId, updateDto.getStatus());

        return userMapper.toResponseDto(updatedUserEntity);
    }

    public UserEntity getUserEntity(Long userId) {
        log.debug("Fetching user entity by id: {}", userId);

        return userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("User not found with id: {}", userId);
                    return new ResourceNotFoundException("User not found with this userId: " + userId);
                });
    }

    public boolean userExists(Long userId) {
        boolean exists = userRepository.existsById(userId);
        log.debug("Checked if user exists with id: {} - result: {}", userId, exists);

        return exists;
    }
}
