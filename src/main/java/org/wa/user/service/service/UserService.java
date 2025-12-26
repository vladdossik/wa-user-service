package org.wa.user.service.service;

import org.springframework.data.domain.Pageable;
import org.wa.user.service.dto.common.PageResponse;
import org.wa.user.service.dto.user.UserCreateDto;
import org.wa.user.service.dto.user.UserRegisteredDto;
import org.wa.user.service.dto.user.UserResponseDto;
import org.wa.user.service.dto.user.UserShortInfoDto;
import org.wa.user.service.dto.user.UserUpdateDto;
import org.wa.user.service.dto.user.UserStatusUpdateDto;
import org.wa.user.service.entity.UserEntity;

public interface UserService {
    PageResponse<UserShortInfoDto> getAllUsers(Pageable pageable);

    UserResponseDto getUserById(Long userId);

    PageResponse<UserShortInfoDto> getNonDeletedUsers(Pageable pageable);

    UserResponseDto createUserFromRegisteredEvent(UserRegisteredDto userRegisteredDto);

    UserResponseDto createUser(UserCreateDto userCreateDto);

    UserResponseDto updateUser(Long userId, UserUpdateDto userUpdateDto);

    void deleteUser(Long userId);

    void hardDeleteUser(Long userId);

    UserResponseDto updateUserStatus(Long userId, UserStatusUpdateDto userStatusDto);

    UserEntity getUserEntity(Long userId);

    boolean userExists(Long userId);
}
