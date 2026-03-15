package org.wa.user.service.service;

import org.springframework.data.domain.Pageable;
import org.wa.user.service.dto.common.PageResponse;
import org.wa.user.service.dto.user.UserRegisteredDto;
import org.wa.user.service.dto.user.UserResponseDto;
import org.wa.user.service.dto.user.UserShortInfoDto;
import org.wa.user.service.dto.user.UserUpdateDto;
import org.wa.user.service.dto.user.UserStatusUpdateDto;
import org.wa.user.service.entity.UserEntity;
import java.util.UUID;

public interface UserService {
    PageResponse<UserShortInfoDto> getAllUsers(Pageable pageable);

    UserResponseDto getUserById(UUID externalId);

    PageResponse<UserShortInfoDto> getNonDeletedUsers(Pageable pageable);

    UserResponseDto createUserFromRegisteredEvent(UserRegisteredDto userRegisteredDto);

    UserResponseDto updateUser(UUID externalId, UserUpdateDto userUpdateDto);

    void deleteUser(UUID externalId);

    void hardDeleteUser(UUID externalId);

    UserResponseDto updateUserStatus(UUID externalId, UserStatusUpdateDto userStatusDto);

    UserEntity getUserEntityByExternalId(UUID externalId);

    UserEntity getUserEntity(Long userId);

    boolean userNotExists(Long userId);
}
