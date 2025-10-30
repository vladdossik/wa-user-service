package org.wa.user.service.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wa.user.service.dto.userProfile.UserProfileRequestDto;
import org.wa.user.service.dto.userProfile.UserProfileResponseDto;
import org.wa.user.service.exception.ResourceNotFoundException;
import org.wa.user.service.exception.UserProfileAlreadyExistsException;
import org.wa.user.service.mapper.UserProfileMapper;
import org.wa.user.service.model.User;
import org.wa.user.service.model.UserProfile;
import org.wa.user.service.repository.UserProfileRepository;
import org.wa.user.service.service.UserProfileService;
import org.wa.user.service.service.UserService;

@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {
    private final UserProfileRepository userProfileRepository;
    private final UserService userService;
    private final UserProfileMapper userProfileMapper;
    @Override
    public UserProfileResponseDto getUserProfile(Long userId) {
        UserProfile userProfile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User profile not found for user id: " + userId));
        return userProfileMapper.toResponseDto(userProfile);
    }

    @Override
    @Transactional
    public UserProfileResponseDto createUserProfile(Long userId, UserProfileRequestDto userProfileCreateDto) {
        User user = userService.getUserEntity(userId);

        if (userProfileRepository.existsByUserId(userId)){
            throw new UserProfileAlreadyExistsException("User profile already exists for user id: " + userId);
        }

        UserProfile profile = userProfileMapper.toEntity(userProfileCreateDto);
        profile.setUser(user);

        UserProfile savedProfile = userProfileRepository.save(profile);
        return userProfileMapper.toResponseDto(savedProfile);
    }

    @Override
    @Transactional
    public UserProfileResponseDto updateUserProfile(Long userId, UserProfileRequestDto userProfileUpdateDto) {
        UserProfile profile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User profile not found for user id: " + userId));

        userProfileMapper.updateEntityFromDto(userProfileUpdateDto, profile);
        UserProfile updatedProfile = userProfileRepository.save(profile);
        return userProfileMapper.toResponseDto(updatedProfile);
    }

    @Override
    public void deleteUserProfile(Long userId) {
        UserProfile profile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found"));
        userProfileRepository.delete(profile);
    }
}
