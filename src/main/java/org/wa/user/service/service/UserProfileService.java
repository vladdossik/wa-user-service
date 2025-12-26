package org.wa.user.service.service;

import org.wa.user.service.dto.profile.UserProfileRequestDto;
import org.wa.user.service.dto.profile.UserProfileResponseDto;
import org.wa.user.service.dto.user.UserResponseDto;

public interface UserProfileService {
    UserProfileResponseDto getUserProfile(Long userId);

    UserProfileResponseDto validateUserAndCreate(Long userId, UserProfileRequestDto userProfileRequestDto);

    void createUserProfileFromRegisteredEvent(Long userId, UserResponseDto userResponseDto);

    UserProfileResponseDto createUserProfile(Long userId, UserProfileRequestDto userProfileCreateDto);

    UserProfileResponseDto updateUserProfile(Long userId, UserProfileRequestDto userProfileUpdateDto);

    void deleteUserProfile(Long userId);
}
