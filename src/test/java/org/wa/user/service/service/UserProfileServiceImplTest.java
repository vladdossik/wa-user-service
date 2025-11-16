package org.wa.user.service.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.wa.user.service.dto.profile.UserProfileRequestDto;
import org.wa.user.service.dto.profile.UserProfileResponseDto;
import org.wa.user.service.entity.UserEntity;
import org.wa.user.service.entity.UserProfileEntity;
import org.wa.user.service.exception.ResourceNotFoundException;
import org.wa.user.service.exception.UserProfileAlreadyExistsException;
import org.wa.user.service.mapper.UserProfileMapper;
import org.wa.user.service.repository.UserProfileRepository;
import org.wa.user.service.service.impl.UserProfileServiceImpl;
import org.wa.user.service.util.Initializer;
import java.util.Optional;
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

    @InjectMocks
    private UserProfileServiceImpl userProfileService;

    private UserEntity user;
    private UserProfileEntity userProfile;
    private UserProfileRequestDto userProfileRequestDto;
    private UserProfileResponseDto userProfileResponseDto;

    @BeforeEach
    void setUp() {
        user = Initializer.createTestUser();
        userProfile = Initializer.createTestUserProfile(user);
        userProfileRequestDto = Initializer.createTestUserProfileRequestDto();
        userProfileResponseDto = Initializer.createTestUserProfileResponseDto();
    }

    @Test
    void getUserProfileTest_success() {
        when(userProfileRepository.findByUserId(1L))
                .thenReturn(Optional.of(userProfile));
        when(userProfileMapper.toResponseDto(userProfile)).thenReturn(userProfileResponseDto);

        UserProfileResponseDto response = userProfileService.getUserProfile(1L);

        assertNotNull(response);
        assertEquals("John", response.getFirstName());
        assertEquals("Smith", response.getLastName());
        verify(userProfileRepository, times(1)).findByUserId(1L);
    }

    @Test
    void getUserProfileTest_profileNotFound() {
        when(userProfileRepository.findByUserId(1L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> userProfileService.getUserProfile(1L));
    }

    @Test
    void createUserProfileTest_success() {
        when(userService.getUserEntity(1L)).thenReturn(user);
        when(userProfileRepository.existsByUserId(1L)).thenReturn(false);
        when(userProfileMapper.toEntity(userProfileRequestDto)).thenReturn(userProfile);
        when(userProfileRepository.save(userProfile)).thenReturn(userProfile);
        when(userProfileMapper.toResponseDto(userProfile)).thenReturn(userProfileResponseDto);

        UserProfileResponseDto response = userProfileService.createUserProfile(
                1L, userProfileRequestDto);

        assertNotNull(response);
        assertEquals(user, userProfile.getUser());
        verify(userProfileRepository, times(1)).save(userProfile);
    }

    @Test
    void createUserProfileTest_profileAlreadyExists() {
        when(userService.getUserEntity(1L)).thenReturn(user);
        when(userProfileRepository.existsByUserId(1L)).thenReturn(true);

        assertThrows(UserProfileAlreadyExistsException.class,
                () -> userProfileService.createUserProfile(1L, userProfileRequestDto));
    }

    @Test
    void updateUserProfileTest_success() {
        when(userProfileRepository.findByUserId(1L))
                .thenReturn(Optional.of(userProfile));
        when(userProfileRepository.save(userProfile)).thenReturn(userProfile);
        when(userProfileMapper.toResponseDto(userProfile)).thenReturn(userProfileResponseDto);

        UserProfileResponseDto response = userProfileService.updateUserProfile(
                1L, userProfileRequestDto);

        assertNotNull(response);
        verify(userProfileMapper,times(1)).updateEntityFromDto(userProfileRequestDto, userProfile);
        verify(userProfileRepository, times(1)).save(userProfile);
    }

    @Test
    void updateUserProfileTest_profileNotFound() {
        when(userProfileRepository.findByUserId(1L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> userProfileService.updateUserProfile(1L, userProfileRequestDto));
    }

    @Test
    void deleteUserProfileTest_success() {
        when(userProfileRepository.findByUserId(1L))
                .thenReturn(Optional.of(userProfile));

        userProfileService.deleteUserProfile(1L);

        verify(userProfileRepository, times(1)).delete(userProfile);
    }

    @Test
    void deleteUserProfileTest_profileNotFound() {
        when(userProfileRepository.findByUserId(1L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> userProfileService.deleteUserProfile(1L));
    }
}