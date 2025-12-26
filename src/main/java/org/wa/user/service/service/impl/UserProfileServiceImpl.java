package org.wa.user.service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wa.user.service.config.UserAccessService;
import org.wa.user.service.dto.profile.UserProfileRequestDto;
import org.wa.user.service.dto.profile.UserProfileResponseDto;
import org.wa.user.service.dto.user.UserResponseDto;
import org.wa.user.service.exception.ResourceNotFoundException;
import org.wa.user.service.exception.UserProfileAlreadyExistsException;
import org.wa.user.service.mapper.UserProfileMapper;
import org.wa.user.service.entity.UserEntity;
import org.wa.user.service.entity.UserProfileEntity;
import org.wa.user.service.repository.UserProfileRepository;
import org.wa.user.service.service.UserProfileService;
import org.wa.user.service.service.UserService;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {
    private final UserProfileRepository userProfileRepository;
    private final UserService userService;
    private final UserProfileMapper userProfileMapper;
    private final UserAccessService accessService;

    @Override
    @Transactional(readOnly = true)
    public UserProfileResponseDto getUserProfile(Long userId) {
        log.info("Getting user profile for user id: {}", userId);

        accessService.checkUser(userId);

        UserProfileEntity profile = getProfile(userId);
        UserProfileResponseDto response = userProfileMapper.toResponseDto(profile);
        log.info("Successfully retrieved profile for user id: {}", userId);

        return response;
    }

    @Override
    @Transactional
    public UserProfileResponseDto validateUserAndCreate(Long userId, UserProfileRequestDto userProfileRequestDto) {
        log.info("Starting user validation before profile create for user id: {}", userId);

        accessService.checkUser(userId);
        return createUserProfile(userId, userProfileRequestDto);
    }

    @Override
    @Transactional
    public void createUserProfileFromRegisteredEvent(Long userId, UserResponseDto userResponseDto) {
        createUserProfile(userId, userProfileMapper.toRequestDto(userResponseDto));
    }

    @Override
    @Transactional
    public UserProfileResponseDto createUserProfile(Long userId, UserProfileRequestDto userProfileCreateDto) {
        log.info("Creating user profile for user id: {}", userId);

        UserEntity userEntity = userService.getUserEntity(userId);
        if (userProfileRepository.existsByUserId(userId)) {
            log.warn("Attempt to create duplicate profile for user id: {}", userId);
            throw new UserProfileAlreadyExistsException("User profile already exists for user id: " + userId);
        }

        log.debug("Mapping UserProfileRequestDto to UserProfileEntity");
        UserProfileEntity profile = userProfileMapper.toEntity(userProfileCreateDto);
        profile.setUser(userEntity);

        log.debug("Saving user profile to database");
        UserProfileEntity savedProfile = userProfileRepository.save(profile);
        UserProfileResponseDto response = userProfileMapper.toResponseDto(savedProfile);
        log.info("Successfully created user profile with id: {} for user id: {}",
                savedProfile.getId(), userId);

        return response;
    }

    @Override
    @Transactional
    public UserProfileResponseDto updateUserProfile(Long userId, UserProfileRequestDto userProfileUpdateDto) {
        log.info("Updating user profile for user id: {}", userId);

        accessService.checkUser(userId);

        log.debug("Mapping UserProfileRequestDto to existing UserProfileEntity");
        userProfileMapper.updateEntityFromDto(userProfileUpdateDto, getProfile(userId));

        log.debug("Saving updated user profile to database");
        UserProfileEntity updatedProfile = userProfileRepository.save(getProfile(userId));
        log.info("Successfully updated user profile for user id: {}", userId);

        return userProfileMapper.toResponseDto(updatedProfile);
    }

    @Override
    @Transactional
    public void deleteUserProfile(Long userId) {
        log.info("Deleting user profile for user id: {}", userId);

        accessService.checkUser(userId);

        userProfileRepository.delete(getProfile(userId));
        log.info("Successfully deleted user profile for user id: {}", userId);
    }

    private UserProfileEntity getProfile(Long userId) {
        log.debug("Fetching user profile for user id: {}", userId);

        return userProfileRepository.findByUserId(userId).orElseThrow(() -> {
            log.warn("User profile not found for user id: {}", userId);
            return new ResourceNotFoundException("User profile not found for user id: " + userId);
        });
    }
}
