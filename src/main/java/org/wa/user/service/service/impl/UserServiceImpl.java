package org.wa.user.service.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wa.user.service.dto.user.UserCreateDto;
import org.wa.user.service.dto.user.UserResponseDto;
import org.wa.user.service.dto.user.UserShortInfoDto;
import org.wa.user.service.dto.user.UserUpdateDto;
import org.wa.user.service.dto.user.UserStatusUpdateDto;
import org.wa.user.service.exception.AttributeDuplicateException;
import org.wa.user.service.exception.ResourceNotFoundException;
import org.wa.user.service.mapper.UserMapper;
import org.wa.user.service.model.User;
import org.wa.user.service.model.enumeration.StatusEnum;
import org.wa.user.service.repository.UserRepository;
import org.wa.user.service.service.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public Page<UserShortInfoDto> getAllUsers(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);

        return users.map(userMapper::toShortInfoDto);
    }

    @Override
    public Page<UserShortInfoDto> getNonDeletedUsers(Pageable pageable) {
        Page<User> users = userRepository.findByStatusNot(StatusEnum.DELETED, pageable);

        return users.map(userMapper::toShortInfoDto);
    }

    @Override
    public UserResponseDto getUserById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User not found with this userId " + userId));

        if (user.getStatus() == StatusEnum.DELETED) {
            throw new ResourceNotFoundException("User not found with userId " + userId);
        }

        return userMapper.toResponseDto(user);
    }

    @Override
    @Transactional
    public UserResponseDto createUser(UserCreateDto createDto) {
        if (userRepository.existsByEmail(createDto.getEmail())) {
            throw new AttributeDuplicateException("Email is already exists: " + createDto.getEmail());
        }
        if (userRepository.existsByPhone(createDto.getPhone())) {
            throw new AttributeDuplicateException("Phone is already exists: " + createDto.getPhone());
        }

        User user = userMapper.toEntity(createDto);
        user.setStatus(StatusEnum.ACTIVE);

        User savedUser = userRepository.save(user);

        return userMapper.toResponseDto(savedUser);
    }

    @Override
    @Transactional
    public UserResponseDto updateUser(Long userId, UserUpdateDto updateDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with this userId: " + userId));

        if (user.getStatus() == StatusEnum.DELETED) {
            throw new ResourceNotFoundException("Cannot update deleted user with userId: " + userId);
        }

        if (updateDto.getEmail() != null &&
                !user.getEmail().equals(updateDto.getEmail()) &&
                userRepository.existsByEmailAndIdNot(updateDto.getEmail(), userId)) {
            throw new AttributeDuplicateException("Email already exists: " + updateDto.getEmail());
        }

        if (updateDto.getPhone() != null &&
                !user.getPhone().equals(updateDto.getPhone()) &&
                userRepository.existsByPhoneAndIdNot(updateDto.getPhone(), userId)) {
            throw new AttributeDuplicateException("Phone already exists: " + updateDto.getPhone());
        }

        userMapper.updateEntityFromDto(updateDto, user);
        User updatedUser = userRepository.save(user);
        return userMapper.toResponseDto(updatedUser);
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with userId: " + userId));

        user.setStatus(StatusEnum.DELETED);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void hardDeleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with this userId" + userId);
        }
        userRepository.deleteById(userId);
    }

    @Override
    @Transactional
    public UserResponseDto updateUserStatus(Long userId, UserStatusUpdateDto updateDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with this userId: " + userId));
        user.setStatus(updateDto.getStatus());
        User updatedUser = userRepository.save(user);
        return userMapper.toResponseDto(updatedUser);
    }

    public User getUserEntity(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with this userId: " + userId));
    }

    public boolean userExists(Long userId) {
        return userRepository.existsById(userId);
    }
}
