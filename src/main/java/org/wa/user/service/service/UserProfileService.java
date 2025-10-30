package org.wa.user.service.service;

import org.wa.user.service.dto.userProfile.UserProfileRequestDto;
import org.wa.user.service.dto.userProfile.UserProfileResponseDto;

public interface UserProfileService {
    UserProfileResponseDto getUserProfile(Long userId);

    UserProfileResponseDto createUserProfile(Long userId, UserProfileRequestDto userProfileCreateDto);

    UserProfileResponseDto updateUserProfile(Long userId, UserProfileRequestDto userProfileUpdateDto);

    void deleteUserProfile(Long userId);
}
