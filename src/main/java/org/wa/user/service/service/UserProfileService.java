package org.wa.user.service.service;

import org.wa.user.service.dto.profile.UserProfileRequestDto;
import org.wa.user.service.dto.profile.UserProfileResponseDto;

public interface UserProfileService {
    UserProfileResponseDto getUserProfile(Long userId);

    UserProfileResponseDto validateUserAndCreate(Long userId, UserProfileRequestDto userProfileRequestDto);

    UserProfileResponseDto createUserProfile(Long userId, UserProfileRequestDto userProfileCreateDto);

    UserProfileResponseDto updateUserProfile(Long userId, UserProfileRequestDto userProfileUpdateDto);

    void deleteUserProfile(Long userId);
}
