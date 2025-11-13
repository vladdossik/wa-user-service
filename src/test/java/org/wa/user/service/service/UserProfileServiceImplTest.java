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
    void getUserProfileSuccessTest() {
        when(userProfileRepository.findByUserId(Initializer.TEST_ID))
                .thenReturn(Optional.of(userProfile));
        when(userProfileMapper.toResponseDto(userProfile)).thenReturn(userProfileResponseDto);

        UserProfileResponseDto response = userProfileService.getUserProfile(Initializer.TEST_ID);

        assertNotNull(response);
        assertEquals(Initializer.TEST_FIRST_NAME, response.getFirstName());
        assertEquals(Initializer.TEST_LAST_NAME, response.getLastName());
        verify(userProfileRepository).findByUserId(Initializer.TEST_ID);
    }

    @Test
    void getUserProfileWhenProfileNotFoundTest() {
        when(userProfileRepository.findByUserId(Initializer.TEST_ID))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> userProfileService.getUserProfile(Initializer.TEST_ID));
    }

    @Test
    void createUserProfileSuccessTest() {
        when(userService.getUserEntity(Initializer.TEST_ID)).thenReturn(user);
        when(userProfileRepository.existsByUserId(Initializer.TEST_ID)).thenReturn(false);
        when(userProfileMapper.toEntity(userProfileRequestDto)).thenReturn(userProfile);
        when(userProfileRepository.save(userProfile)).thenReturn(userProfile);
        when(userProfileMapper.toResponseDto(userProfile)).thenReturn(userProfileResponseDto);

        UserProfileResponseDto response = userProfileService.createUserProfile(
                Initializer.TEST_ID, userProfileRequestDto);

        assertNotNull(response);
        assertEquals(user, userProfile.getUser());
        verify(userProfileRepository).save(userProfile);
    }

    @Test
    void createUserProfileWhenProfileAlreadyExistsTest() {
        when(userService.getUserEntity(Initializer.TEST_ID)).thenReturn(user);
        when(userProfileRepository.existsByUserId(Initializer.TEST_ID)).thenReturn(true);

        assertThrows(UserProfileAlreadyExistsException.class,
                () -> userProfileService.createUserProfile(Initializer.TEST_ID, userProfileRequestDto));
    }

    @Test
    void updateUserProfileSuccessTest() {
        when(userProfileRepository.findByUserId(Initializer.TEST_ID))
                .thenReturn(Optional.of(userProfile));
        when(userProfileRepository.save(userProfile)).thenReturn(userProfile);
        when(userProfileMapper.toResponseDto(userProfile)).thenReturn(userProfileResponseDto);

        UserProfileResponseDto response = userProfileService.updateUserProfile(
                Initializer.TEST_ID, userProfileRequestDto);

        assertNotNull(response);
        verify(userProfileMapper).updateEntityFromDto(userProfileRequestDto, userProfile);
        verify(userProfileRepository).save(userProfile);
    }

    @Test
    void updateUserProfileWhenProfileNotFoundTest() {
        when(userProfileRepository.findByUserId(Initializer.TEST_ID))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> userProfileService.updateUserProfile(Initializer.TEST_ID, userProfileRequestDto));
    }

    @Test
    void deleteUserProfileSuccessTest() {
        when(userProfileRepository.findByUserId(Initializer.TEST_ID))
                .thenReturn(Optional.of(userProfile));

        userProfileService.deleteUserProfile(Initializer.TEST_ID);

        verify(userProfileRepository).delete(userProfile);
    }

    @Test
    void deleteUserProfileWhenProfileNotFoundTest() {
        when(userProfileRepository.findByUserId(Initializer.TEST_ID))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> userProfileService.deleteUserProfile(Initializer.TEST_ID));
    }
}