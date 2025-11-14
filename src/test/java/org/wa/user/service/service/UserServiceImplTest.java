package org.wa.user.service.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.wa.user.service.dto.common.PageResponse;
import org.wa.user.service.dto.user.UserCreateDto;
import org.wa.user.service.dto.user.UserResponseDto;
import org.wa.user.service.dto.user.UserShortInfoDto;
import org.wa.user.service.dto.user.UserStatusUpdateDto;
import org.wa.user.service.dto.user.UserUpdateDto;
import org.wa.user.service.entity.UserEntity;
import org.wa.user.service.entity.enumeration.Status;
import org.wa.user.service.exception.AttributeDuplicateException;
import org.wa.user.service.exception.ResourceNotFoundException;
import org.wa.user.service.mapper.UserMapper;
import org.wa.user.service.repository.UserRepository;
import org.wa.user.service.service.impl.UserServiceImpl;
import org.wa.user.service.util.Initializer;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private UserEntity user;
    private UserEntity deletedUser;
    private UserCreateDto createDto;
    private UserUpdateDto updateDto;
    private Pageable pageable;
    private Page<UserEntity> userPage;
    private UserShortInfoDto userShortInfoDto;
    private UserResponseDto userResponseDto;
    private UserStatusUpdateDto statusUpdateDto;
    private UserUpdateDto updateDtoWithNewValues;
    private UserUpdateDto updateDtoWithExistingEmail;

    @BeforeEach
    void setUp() {
        user = Initializer.createTestUser();
        createDto = Initializer.createTestUserCreateDto();
        updateDto = Initializer.createTestUserUpdateDto();
        pageable = Initializer.createDefaultPageable();
        userPage = Initializer.createUserPage(user);
        userShortInfoDto = Initializer.createTestUserShortInfoDto();
        userResponseDto = Initializer.createTestUserResponseDto();
        deletedUser = Initializer.createDeletedUser();
        statusUpdateDto = Initializer.createTestUserStatusUpdateDto();
        updateDtoWithNewValues = Initializer.createTestUserUpdateWithNewValues();
        updateDtoWithExistingEmail = Initializer.createTestUserUpdateWithExistingEmail();
    }

    @Test
    void getAllUsersTest_success() {
        when(userRepository.findAll(pageable)).thenReturn(userPage);
        when(userMapper.toShortInfoDto(user)).thenReturn(userShortInfoDto);

        PageResponse<UserShortInfoDto> response = userService.getAllUsers(pageable);

        assertNotNull(response);
        assertEquals(1, response.getContent().size());
        verify(userRepository, times(1)).findAll(pageable);
    }

    @Test
    void getNonDeletedUsersTest_success() {
        when(userRepository.findByStatusNot(Status.DELETED, pageable)).thenReturn(userPage);
        when(userMapper.toShortInfoDto(user)).thenReturn(userShortInfoDto);

        PageResponse<UserShortInfoDto> response = userService.getNonDeletedUsers(pageable);

        assertNotNull(response);
        assertEquals(1, response.getContent().size());
        verify(userRepository, times(1)).findByStatusNot(Status.DELETED, pageable);
    }

    @Test
    void getUserByIdTest_success() {
        when(userRepository.findById(Initializer.TEST_ID)).thenReturn(Optional.of(user));
        when(userMapper.toResponseDto(user)).thenReturn(userResponseDto);

        UserResponseDto response = userService.getUserById(Initializer.TEST_ID);

        assertNotNull(response);
        assertEquals(Initializer.TEST_ID, response.getId());
        assertEquals(Initializer.TEST_EMAIL, response.getEmail());
        verify(userRepository, times(1)).findById(Initializer.TEST_ID);
    }

    @Test
    void getUserByIdTest_userNotFound() {
        when(userRepository.findById(Initializer.TEST_ID)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> userService.getUserById(Initializer.TEST_ID));
    }

    @Test
    void getUserByIdTest_userDeleted() {
        when(userRepository.findById(Initializer.TEST_ID)).thenReturn(Optional.of(deletedUser));

        assertThrows(ResourceNotFoundException.class,
                () -> userService.getUserById(Initializer.TEST_ID));
    }

    @Test
    void createUserTest_success() {
        when(userRepository.existsByEmail(Initializer.TEST_EMAIL)).thenReturn(false);
        when(userRepository.existsByPhone(Initializer.TEST_PHONE)).thenReturn(false);
        when(userMapper.toEntity(createDto)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toResponseDto(user)).thenReturn(userResponseDto);

        UserResponseDto response = userService.createUser(createDto);

        assertNotNull(response);
        assertEquals(Status.ACTIVE, user.getStatus());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void createUserTest_emailExists() {
        when(userRepository.existsByEmail(Initializer.TEST_EMAIL)).thenReturn(true);

        assertThrows(AttributeDuplicateException.class,
                () -> userService.createUser(createDto));
    }

    @Test
    void createUserTest_phoneExists() {
        when(userRepository.existsByEmail(Initializer.TEST_EMAIL)).thenReturn(false);
        when(userRepository.existsByPhone(Initializer.TEST_PHONE)).thenReturn(true);

        assertThrows(AttributeDuplicateException.class,
                () -> userService.createUser(createDto));
    }

    @Test
    void updateUserTest_success() {
        when(userRepository.findById(Initializer.TEST_ID)).thenReturn(Optional.of(user));
        when(userRepository.existsByEmailAndIdNot("new@email.com", Initializer.TEST_ID))
                .thenReturn(false);
        when(userRepository.existsByPhoneAndIdNot("+79164538677", Initializer.TEST_ID))
                .thenReturn(false);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toResponseDto(user)).thenReturn(userResponseDto);

        UserResponseDto response = userService.updateUser(Initializer.TEST_ID, updateDtoWithNewValues);

        assertNotNull(response);
        verify(userMapper, times(1)).updateEntityFromDto(updateDtoWithNewValues, user);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void updateUserTest_userNotFound() {
        when(userRepository.findById(Initializer.TEST_ID)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> userService.updateUser(Initializer.TEST_ID, updateDto));
    }

    @Test
    void updateUserTest_emailExists() {
        when(userRepository.findById(Initializer.TEST_ID)).thenReturn(Optional.of(user));
        when(userRepository.existsByEmailAndIdNot("existing@email.com", Initializer.TEST_ID))
                .thenReturn(true);

        assertThrows(AttributeDuplicateException.class,
                () -> userService.updateUser(Initializer.TEST_ID, updateDtoWithExistingEmail));
    }

    @Test
    void deleteUserTest_success() {
        when(userRepository.findById(Initializer.TEST_ID)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        userService.deleteUser(Initializer.TEST_ID);

        assertEquals(Status.DELETED, user.getStatus());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void hardDeleteUserTest_success() {
        when(userRepository.existsById(Initializer.TEST_ID)).thenReturn(true);

        userService.hardDeleteUser(Initializer.TEST_ID);

        verify(userRepository, times(1)).deleteById(Initializer.TEST_ID);
    }

    @Test
    void hardDeleteUserTest_userNotFound() {
        when(userRepository.existsById(Initializer.TEST_ID)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                () -> userService.hardDeleteUser(Initializer.TEST_ID));
    }

    @Test
    void updateUserStatusTest_success() {
        when(userRepository.findById(Initializer.TEST_ID)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toResponseDto(user)).thenReturn(userResponseDto);

        UserResponseDto response = userService.updateUserStatus(Initializer.TEST_ID, statusUpdateDto);

        assertNotNull(response);
        assertEquals(Status.ACTIVE, user.getStatus());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void userExistsTest_success() {
        when(userRepository.existsById(Initializer.TEST_ID)).thenReturn(true);

        boolean exists = userService.userExists(Initializer.TEST_ID);

        assertTrue(exists);
        verify(userRepository, times(1)).existsById(Initializer.TEST_ID);
    }
}
