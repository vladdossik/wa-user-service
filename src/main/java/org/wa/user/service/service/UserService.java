package org.wa.user.service.service;

import org.wa.user.service.dto.user.UserCreateDto;
import org.wa.user.service.dto.user.UserResponseDto;
import org.wa.user.service.dto.user.UserShortInfoDto;
import org.wa.user.service.dto.user.UserUpdateDto;
import org.wa.user.service.dto.user.UserStatusUpdateDto;
import org.wa.user.service.model.User;
import java.util.List;

public interface UserService {
    List<UserShortInfoDto> getAllUsers();

    UserResponseDto getUserById(Long userId);

    UserResponseDto createUser(UserCreateDto userCreateDto);

    UserResponseDto updateUser(Long userId, UserUpdateDto userUpdateDto);

    void deleteUser(Long userId);

    UserResponseDto updateUserStatus(Long userId, UserStatusUpdateDto userStatusDto);

    User getUserEntity(Long userId);

    boolean userExists(Long userId);
}
