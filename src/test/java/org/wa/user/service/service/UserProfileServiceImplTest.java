package org.wa.user.service.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.wa.user.service.dto.profile.UserProfileRequestDto;
import org.wa.user.service.dto.profile.UserProfileResponseDto;
import org.wa.user.service.dto.user.UserResponseDto;
import org.wa.user.service.entity.UserEntity;
import org.wa.user.service.entity.UserProfileEntity;
import org.wa.user.service.exception.ResourceNotFoundException;
import org.wa.user.service.exception.UserProfileAlreadyExistsException;
import org.wa.user.service.mapper.UserProfileMapper;
import org.wa.user.service.repository.UserProfileRepository;
import org.wa.user.service.service.impl.UserProfileServiceImpl;
import org.wa.user.service.util.Initializer;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserProfileServiceImplTest {
    @Mock
    private UserProfileRepository userProfileRepository;

    @Mock
    private UserService userService;

    @Mock
    private UserProfileMapper userProfileMapper;

    @Mock
    private UserAccessService accessService;

    @InjectMocks
    private UserProfileServiceImpl userProfileService;

    private UserEntity user;
    private UserProfileEntity userProfile;
    private UserProfileRequestDto userProfileRequestDto;
    private UserProfileResponseDto userProfileResponseDto;
    private UserResponseDto userResponseDto;
    private Long userId;
    private UUID externalId;

    @BeforeEach
    void setUp() {
        user = Initializer.createTestUser();
        userId = user.getId();
        externalId = user.getExternalId();
        userProfile = Initializer.createTestUserProfile(user);
        userProfileRequestDto = Initializer.createTestUserProfileRequestDto();
        userProfileResponseDto = Initializer.createTestUserProfileResponseDto();
        userResponseDto = Initializer.createTestUserResponseDto();
    }

    @Test
    void getUserProfileTest_success() {
        when(userProfileRepository.findByUserId(userId))
                .thenReturn(Optional.of(userProfile));
        when(userProfileMapper.toResponseDto(userProfile)).thenReturn(userProfileResponseDto);

        UserProfileResponseDto response = userProfileService.getUserProfile(userId);

        assertNotNull(response);
        assertEquals("John", response.getFirstName());
        assertEquals("Smith", response.getLastName());
        verify(accessService, times(1)).checkUser(userId);
        verify(userProfileRepository, times(1)).findByUserId(userId);
    }

    @Test
    void getUserProfileTest_profileNotFound() {
        when(userProfileRepository.findByUserId(userId))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> userProfileService.getUserProfile(userId));
        verify(accessService, times(1)).checkUser(userId);
    }

    @Test
    void createUserProfileFromRegisteredEventTest_success() {
        when(userService.getUserEntityByExternalId(externalId)).thenReturn(user);
        when(userProfileRepository.existsByUser(user)).thenReturn(false);
        when(userProfileMapper.toRequestDto(userResponseDto)).thenReturn(userProfileRequestDto);
        when(userProfileMapper.toEntity(userProfileRequestDto)).thenReturn(userProfile);
        when(userProfileRepository.save(userProfile)).thenReturn(userProfile);

        userProfileService.createUserProfileFromRegisteredEvent(externalId, userResponseDto);

        verify(userService, times(1)).getUserEntityByExternalId(externalId);
        verify(userProfileRepository, times(1)).save(userProfile);
    }

    @Test
    void createUserProfileFromRegisteredEventTest_profileAlreadyExists() {
        when(userService.getUserEntityByExternalId(externalId)).thenReturn(user);
        when(userProfileRepository.existsByUser(user)).thenReturn(true);

        assertThrows(UserProfileAlreadyExistsException.class,
                () -> userProfileService.createUserProfileFromRegisteredEvent(externalId, userResponseDto));
    }

    @Test
    void updateUserProfileTest_success() {
        when(userProfileRepository.findByUserId(userId))
                .thenReturn(Optional.of(userProfile));
        when(userProfileRepository.save(userProfile)).thenReturn(userProfile);
        when(userProfileMapper.toResponseDto(userProfile)).thenReturn(userProfileResponseDto);

        UserProfileResponseDto response = userProfileService.updateUserProfile(
                userId, userProfileRequestDto);

        assertNotNull(response);
        verify(accessService, times(1)).checkUser(userId);
        verify(userProfileMapper,times(1)).updateEntityFromDto(userProfileRequestDto, userProfile);
        verify(userProfileRepository, times(1)).save(userProfile);
    }

    @Test
    void updateUserProfileTest_profileNotFound() {
        when(userProfileRepository.findByUserId(userId))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> userProfileService.updateUserProfile(userId, userProfileRequestDto));
        verify(accessService, times(1)).checkUser(userId);
    }

    @Test
    void deleteUserProfileTest_success() {
        when(userProfileRepository.findByUserId(1L))
                .thenReturn(Optional.of(userProfile));

        userProfileService.deleteUserProfile(userId);

        verify(accessService, times(1)).checkUser(userId);
        verify(userProfileRepository, times(1)).delete(userProfile);
    }

    @Test
    void deleteUserProfileTest_profileNotFound() {
        when(userProfileRepository.findByUserId(userId))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> userProfileService.deleteUserProfile(userId));
        verify(accessService, times(1)).checkUser(userId);
    }
}