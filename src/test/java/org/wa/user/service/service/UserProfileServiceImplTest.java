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
        when(userProfileRepository.findByUserId(Initializer.TEST_ID))
                .thenReturn(Optional.of(userProfile));
        when(userProfileMapper.toResponseDto(userProfile)).thenReturn(userProfileResponseDto);

        UserProfileResponseDto response = userProfileService.getUserProfile(Initializer.TEST_ID);

        assertNotNull(response);
        assertEquals(Initializer.TEST_FIRST_NAME, response.getFirstName());
        assertEquals(Initializer.TEST_LAST_NAME, response.getLastName());
        verify(userProfileRepository, times(1)).findByUserId(Initializer.TEST_ID);
    }

    @Test
    void getUserProfileTest_profileNotFound() {
        when(userProfileRepository.findByUserId(Initializer.TEST_ID))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> userProfileService.getUserProfile(Initializer.TEST_ID));
    }

    @Test
    void createUserProfileTest_success() {
        when(userService.getUserEntity(Initializer.TEST_ID)).thenReturn(user);
        when(userProfileRepository.existsByUserId(Initializer.TEST_ID)).thenReturn(false);
        when(userProfileMapper.toEntity(userProfileRequestDto)).thenReturn(userProfile);
        when(userProfileRepository.save(userProfile)).thenReturn(userProfile);
        when(userProfileMapper.toResponseDto(userProfile)).thenReturn(userProfileResponseDto);

        UserProfileResponseDto response = userProfileService.createUserProfile(
                Initializer.TEST_ID, userProfileRequestDto);

        assertNotNull(response);
        assertEquals(user, userProfile.getUser());
        verify(userProfileRepository, times(1)).save(userProfile);
    }

    @Test
    void createUserProfileTest_profileAlreadyExists() {
        when(userService.getUserEntity(Initializer.TEST_ID)).thenReturn(user);
        when(userProfileRepository.existsByUserId(Initializer.TEST_ID)).thenReturn(true);

        assertThrows(UserProfileAlreadyExistsException.class,
                () -> userProfileService.createUserProfile(Initializer.TEST_ID, userProfileRequestDto));
    }

    @Test
    void updateUserProfileTest_success() {
        when(userProfileRepository.findByUserId(Initializer.TEST_ID))
                .thenReturn(Optional.of(userProfile));
        when(userProfileRepository.save(userProfile)).thenReturn(userProfile);
        when(userProfileMapper.toResponseDto(userProfile)).thenReturn(userProfileResponseDto);

        UserProfileResponseDto response = userProfileService.updateUserProfile(
                Initializer.TEST_ID, userProfileRequestDto);

        assertNotNull(response);
        verify(userProfileMapper,times(1)).updateEntityFromDto(userProfileRequestDto, userProfile);
        verify(userProfileRepository, times(1)).save(userProfile);
    }

    @Test
    void updateUserProfileTest_profileNotFound() {
        when(userProfileRepository.findByUserId(Initializer.TEST_ID))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> userProfileService.updateUserProfile(Initializer.TEST_ID, userProfileRequestDto));
    }

    @Test
    void deleteUserProfileTest_success() {
        when(userProfileRepository.findByUserId(Initializer.TEST_ID))
                .thenReturn(Optional.of(userProfile));

        userProfileService.deleteUserProfile(Initializer.TEST_ID);

        verify(userProfileRepository, times(1)).delete(userProfile);
    }

    @Test
    void deleteUserProfileTest_profileNotFound() {
        when(userProfileRepository.findByUserId(Initializer.TEST_ID))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> userProfileService.deleteUserProfile(Initializer.TEST_ID));
    }
}