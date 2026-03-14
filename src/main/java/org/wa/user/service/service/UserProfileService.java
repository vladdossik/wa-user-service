package org.wa.user.service.service;

import org.wa.user.service.dto.profile.UserProfileRequestDto;
import org.wa.user.service.dto.profile.UserProfileResponseDto;
import org.wa.user.service.dto.user.UserResponseDto;
import java.util.UUID;

public interface UserProfileService {
    UserProfileResponseDto getUserProfile(Long userId);

    void createUserProfileFromRegisteredEvent(UUID userId, UserResponseDto userResponseDto);

    UserProfileResponseDto updateUserProfile(Long userId, UserProfileRequestDto userProfileUpdateDto);

    void deleteUserProfile(Long userId);
}
