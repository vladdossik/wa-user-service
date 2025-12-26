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
import org.wa.user.service.config.UserAccessService;
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

    @Mock
    private UserAccessService accessService;

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
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toResponseDto(user)).thenReturn(userResponseDto);

        UserResponseDto response = userService.getUserById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("test@email.com", response.getEmail());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void getUserByIdTest_userNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> userService.getUserById(1L));
    }

    @Test
    void getUserByIdTest_userDeleted() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(deletedUser));

        assertThrows(ResourceNotFoundException.class,
                () -> userService.getUserById(1L));
    }

    @Test
    void createUserTest_success() {
        when(userRepository.existsByEmail("test@email.com")).thenReturn(false);
        when(userRepository.existsByPhone("+79164538676")).thenReturn(false);
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
        when(userRepository.existsByEmail("test@email.com")).thenReturn(true);

        assertThrows(AttributeDuplicateException.class,
                () -> userService.createUser(createDto));
    }

    @Test
    void createUserTest_phoneExists() {
        when(userRepository.existsByEmail("test@email.com")).thenReturn(false);
        when(userRepository.existsByPhone("+79164538676")).thenReturn(true);

        assertThrows(AttributeDuplicateException.class,
                () -> userService.createUser(createDto));
    }

    @Test
    void updateUserTest_success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.existsByEmailAndIdNot("new@email.com", 1L))
                .thenReturn(false);
        when(userRepository.existsByPhoneAndIdNot("+79164538677", 1L))
                .thenReturn(false);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toResponseDto(user)).thenReturn(userResponseDto);

        UserResponseDto response = userService.updateUser(1L, updateDtoWithNewValues);

        assertNotNull(response);
        verify(userMapper, times(1)).updateEntityFromDto(updateDtoWithNewValues, user);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void updateUserTest_userNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> userService.updateUser(1L, updateDto));
    }

    @Test
    void updateUserTest_emailExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.existsByEmailAndIdNot("existing@email.com", 1L))
                .thenReturn(true);

        assertThrows(AttributeDuplicateException.class,
                () -> userService.updateUser(1L, updateDtoWithExistingEmail));
    }

    @Test
    void deleteUserTest_success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        userService.deleteUser(1L);

        assertEquals(Status.DELETED, user.getStatus());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void hardDeleteUserTest_success() {
        when(userRepository.existsById(1L)).thenReturn(true);

        userService.hardDeleteUser(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void hardDeleteUserTest_userNotFound() {
        when(userRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                () -> userService.hardDeleteUser(1L));
    }

    @Test
    void updateUserStatusTest_success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toResponseDto(user)).thenReturn(userResponseDto);

        UserResponseDto response = userService.updateUserStatus(1L, statusUpdateDto);

        assertNotNull(response);
        assertEquals(Status.ACTIVE, user.getStatus());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void userExistsTest_success() {
        when(userRepository.existsById(1L)).thenReturn(true);

        boolean exists = userService.userExists(1L);

        assertTrue(exists);
        verify(userRepository, times(1)).existsById(1L);
    }
}
