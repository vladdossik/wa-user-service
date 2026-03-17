package org.wa.user.service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wa.user.service.service.UserAccessService;
import org.wa.user.service.dto.common.PageResponse;
import org.wa.user.service.dto.user.UserCreateDto;
import org.wa.user.service.dto.user.UserRegisteredDto;
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
import java.util.UUID;

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
    public UserResponseDto getUserById(UUID externalId) {
        log.info("Getting user by id: {}", externalId);

        accessService.checkUser(externalId);

        UserEntity userEntity = getUserEntityByExternalId(externalId);

        if (userEntity.getStatus() == Status.DELETED) {
            log.warn("Tried to access deleted user with id: {}", externalId);
            throw new ResourceNotFoundException("User not found with userId " + externalId);
        }

        UserResponseDto response = userMapper.toResponseDto(userEntity);
        log.info("Successfully retrieved user with external id: {}, email: {}", externalId, userEntity.getEmail());

        return response;
    }

    @Override
    @Transactional
    public UserResponseDto createUserFromRegisteredEvent(UserRegisteredDto userRegisteredDto) {
        return createUser(userMapper.toCreateDtoFromTopic(userRegisteredDto));
    }

    @Override
    @Transactional
    public UserResponseDto updateUser(UUID externalId, UserUpdateDto updateDto) {
        log.info("Updating user with id: {}", externalId);

        accessService.checkUser(externalId);

        UserEntity userEntity = getUserEntityByExternalId(externalId);

        if (userEntity.getStatus() == Status.DELETED) {
            log.warn("Attempt to update deleted user with id: {}", externalId);
            throw new ResourceNotFoundException("Cannot update deleted user with userId: " + externalId);
        }

        if (updateDto.getEmail() != null &&
                !userEntity.getEmail().equals(updateDto.getEmail()) &&
                userRepository.existsByEmailAndExternalId(updateDto.getEmail(), externalId)) {
            log.warn("Attempt to update user id: {} with duplicate email: {}", externalId, updateDto.getEmail());
            throw new AttributeDuplicateException("Email already exists: " + updateDto.getEmail());
        }

        if (updateDto.getPhone() != null &&
                !userEntity.getPhone().equals(updateDto.getPhone()) &&
                userRepository.existsByPhoneAndExternalId(updateDto.getPhone(), externalId)) {
            log.warn("Attempt to update user id: {} with duplicate phone: {}", externalId, updateDto.getPhone());
            throw new AttributeDuplicateException("Phone already exists: " + updateDto.getPhone());
        }

        log.debug("Mapping UserUpdateDto to existing UserEntity");
        userMapper.updateEntityFromDto(updateDto, userEntity);

        log.debug("Saving updated user to database");
        UserEntity updatedUserEntity = userRepository.save(userEntity);

        UserResponseDto response = userMapper.toResponseDto(updatedUserEntity);
        log.info("Successfully updated user with id: {}", externalId);

        return response;
    }

    @Override
    @Transactional
    public void deleteUser(UUID externalId) {
        log.info("Deleting user with id: {}", externalId);

        accessService.checkUser(externalId);

        UserEntity userEntity = getUserEntityByExternalId(externalId);
        userEntity.setStatus(Status.DELETED);

        userRepository.save(userEntity);
        log.info("Successfully deleted user with id: {}", externalId);
    }

    @Override
    @Transactional
    public void hardDeleteUser(UUID externalId) {
        log.info("Hard deleting user with id: {}", externalId);

        accessService.checkUser(externalId);

        if (!userRepository.existsByExternalId(externalId)) {
            log.warn("Attempt to hard delete non-existent user with id: {}", externalId);
            throw new ResourceNotFoundException("User not found with this userId" + externalId);
        }

        userRepository.deleteByExternalId(externalId);
        log.info("Successfully hard deleted user with id: {}", externalId);
    }

    @Override
    @Transactional
    public UserResponseDto updateUserStatus(UUID externalId, UserStatusUpdateDto updateDto) {
        log.info("Updating status for user id: {} to {}", externalId, updateDto.getStatus());

        accessService.checkUser(externalId);

        UserEntity userEntity = getUserEntityByExternalId(externalId);
        userEntity.setStatus(updateDto.getStatus());

        UserEntity updatedUserEntity = userRepository.save(userEntity);
        log.info("Successfully updated status for user id: {} to {}", externalId, updateDto.getStatus());

        return userMapper.toResponseDto(updatedUserEntity);
    }

    @Override
    public UserEntity getUserEntityByExternalId(UUID externalId) {
        log.debug("Fetching user entity by external id: {}", externalId);

        return userRepository.findByExternalId(externalId)
                .orElseThrow(() -> {
                    log.warn("User not found with external id: {}", externalId);
                    return new ResourceNotFoundException("User not found with this external id: " + externalId);
                });
    }

    @Override
    public UserEntity getUserEntity(Long userId) {
        log.debug("Fetching user entity by id: {}", userId);

        return userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("User not found with id: {}", userId);
                    return new ResourceNotFoundException("User not found with this userId: " + userId);
                });
    }

    @Override
    public boolean userNotExists(Long userId) {
        boolean exists = userRepository.existsById(userId);
        log.debug("Checked if user exists with id: {} - result: {}", userId, exists);

        return !exists;
    }

    private UserResponseDto createUser(UserCreateDto createDto) {
        log.info("Creating new user with email: {}", createDto.getEmail());

        if (userRepository.existsByExternalId(createDto.getExternalId())) {
            log.warn("Attempt to create user with duplicate ID: {}", createDto.getExternalId());
            throw new AttributeDuplicateException("ID is already exists: " + createDto.getExternalId());
        }

        if (userRepository.existsByEmail(createDto.getEmail())) {
            log.warn("Attempt to create user with duplicate email: {}", createDto.getEmail());
            throw new AttributeDuplicateException("Email is already exists: " + createDto.getEmail());
        }

        if (createDto.getPhone() != null && userRepository.existsByPhone(createDto.getPhone())) {
            log.warn("Attempt to create user with duplicate phone: {}", createDto.getPhone());
            throw new AttributeDuplicateException("Phone is already exists: " + createDto.getPhone());
        }

        log.debug("Mapping UserCreateDto to UserEntity");
        UserEntity userEntity = userMapper.toEntity(createDto);
        userEntity.setStatus(Status.ACTIVE);

        log.debug("Saving user to database");
        UserEntity savedUserEntity = userRepository.save(userEntity);
        log.info("Successfully created user with external id: {} and email: {}",
                savedUserEntity.getExternalId(), savedUserEntity.getEmail());

        UserResponseDto response = userMapper.toResponseDto(savedUserEntity);
        log.debug("Successfully mapped UserEntity to UserResponseDto");

        return response;
    }
}
