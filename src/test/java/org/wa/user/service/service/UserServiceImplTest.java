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
import org.wa.user.service.dto.user.UserRegisteredDto;
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
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    private UserUpdateDto updateDto;
    private UserRegisteredDto registeredDto;
    private Pageable pageable;
    private Page<UserEntity> userPage;
    private UserShortInfoDto userShortInfoDto;
    private UserResponseDto userResponseDto;
    private UserStatusUpdateDto statusUpdateDto;
    private UserUpdateDto updateDtoWithNewValues;
    private UserUpdateDto updateDtoWithExistingEmail;
    private UserCreateDto createDto;
    private UUID externalId;

    @BeforeEach
    void setUp() {
        user = Initializer.createTestUser();
        externalId = user.getExternalId();
        createDto = Initializer.createTestUserCreateDto();
        updateDto = Initializer.createTestUserUpdateDto();
        registeredDto = Initializer.createTestUserRegisteredDto();
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
        when(userRepository.findByExternalId(externalId)).thenReturn(Optional.of(user));
        when(userMapper.toResponseDto(user)).thenReturn(userResponseDto);

        UserResponseDto response = userService.getUserById(externalId);

        assertNotNull(response);
        assertEquals(externalId, response.getId());
        assertEquals("test@email.com", response.getEmail());
        verify(accessService, times(1)).checkUser(externalId);
        verify(userRepository, times(1)).findByExternalId(externalId);
    }

    @Test
    void getUserByIdTest_userNotFound() {
        when(userRepository.findByExternalId(externalId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> userService.getUserById(externalId));
        verify(accessService, times(1)).checkUser(externalId);
    }

    @Test
    void getUserByIdTest_userDeleted() {
        when(userRepository.findByExternalId(externalId)).thenReturn(Optional.of(deletedUser));

        assertThrows(ResourceNotFoundException.class,
                () -> userService.getUserById(externalId));
        verify(accessService, times(1)).checkUser(externalId);
    }

    @Test
    void createUserFromRegisteredEventTest_success() {
        when(userMapper.toCreateDtoFromTopic(registeredDto)).thenReturn(createDto);
        when(userRepository.existsByExternalId(createDto.getId())).thenReturn(false);
        when(userRepository.existsByEmail(createDto.getEmail())).thenReturn(false);
        when(userRepository.existsByPhone(createDto.getPhone())).thenReturn(false);
        when(userMapper.toEntity(createDto)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toResponseDto(user)).thenReturn(userResponseDto);

        UserResponseDto response = userService.createUserFromRegisteredEvent(registeredDto);

        assertNotNull(response);
        assertEquals(externalId, response.getId());
        verify(userMapper, times(1)).toCreateDtoFromTopic(registeredDto);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void createUserFromRegisteredEventTest_externalIdExists() {
        when(userMapper.toCreateDtoFromTopic(registeredDto)).thenReturn(createDto);
        when(userRepository.existsByExternalId(createDto.getId())).thenReturn(true);

        assertThrows(AttributeDuplicateException.class,
                () -> userService.createUserFromRegisteredEvent(registeredDto));
    }

    @Test
    void createUserFromRegisteredEventTest_emailExists() {
        when(userMapper.toCreateDtoFromTopic(registeredDto)).thenReturn(createDto);
        when(userRepository.existsByExternalId(createDto.getId())).thenReturn(false);
        when(userRepository.existsByEmail(createDto.getEmail())).thenReturn(true);

        assertThrows(AttributeDuplicateException.class,
                () -> userService.createUserFromRegisteredEvent(registeredDto));
    }

    @Test
    void createUserFromRegisteredEventTest_phoneExists() {
        when(userMapper.toCreateDtoFromTopic(registeredDto)).thenReturn(createDto);
        when(userRepository.existsByExternalId(createDto.getId())).thenReturn(false);
        when(userRepository.existsByEmail(createDto.getEmail())).thenReturn(false);
        when(userRepository.existsByPhone(createDto.getPhone())).thenReturn(true);

        assertThrows(AttributeDuplicateException.class,
                () -> userService.createUserFromRegisteredEvent(registeredDto));
    }

    @Test
    void updateUserTest_success() {
        when(userRepository.findByExternalId(externalId)).thenReturn(Optional.of(user));
        when(userRepository.existsByEmailAndExternalId(updateDtoWithNewValues.getEmail(), user.getExternalId()))
                .thenReturn(false);
        when(userRepository.existsByPhoneAndExternalId(updateDtoWithNewValues.getPhone(), user.getExternalId()))
                .thenReturn(false);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toResponseDto(user)).thenReturn(userResponseDto);

        UserResponseDto response = userService.updateUser(externalId, updateDtoWithNewValues);

        assertNotNull(response);
        verify(accessService, times(1)).checkUser(externalId);
        verify(userMapper, times(1)).updateEntityFromDto(updateDtoWithNewValues, user);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void updateUserTest_userNotFound() {
        when(userRepository.findByExternalId(externalId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> userService.updateUser(externalId, updateDto));
        verify(accessService, times(1)).checkUser(externalId);
    }

    @Test
    void updateUserTest_userDeleted() {
        when(userRepository.findByExternalId(externalId)).thenReturn(Optional.of(deletedUser));

        assertThrows(ResourceNotFoundException.class,
                () -> userService.updateUser(externalId, updateDto));
        verify(accessService, times(1)).checkUser(externalId);
    }

    @Test
    void updateUserTest_emailExists() {
        when(userRepository.findByExternalId(externalId)).thenReturn(Optional.of(user));
        when(userRepository.existsByEmailAndExternalId(updateDtoWithExistingEmail.getEmail(), user.getExternalId()))
                .thenReturn(true);

        assertThrows(AttributeDuplicateException.class,
                () -> userService.updateUser(externalId, updateDtoWithExistingEmail));
        verify(accessService, times(1)).checkUser(externalId);
    }

    @Test
    void updateUserTest_phoneExists() {
        when(userRepository.findByExternalId(externalId)).thenReturn(Optional.of(user));
        when(userRepository.existsByPhoneAndExternalId(updateDtoWithNewValues.getPhone(), user.getExternalId()))
                .thenReturn(true);

        assertThrows(AttributeDuplicateException.class,
                () -> userService.updateUser(externalId, updateDtoWithNewValues));
        verify(accessService, times(1)).checkUser(externalId);
    }

    @Test
    void deleteUserTest_success() {
        when(userRepository.findByExternalId(externalId)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        userService.deleteUser(externalId);

        assertEquals(Status.DELETED, user.getStatus());
        verify(accessService, times(1)).checkUser(externalId);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void deleteUserTest_userNotFound() {
        when(userRepository.findByExternalId(externalId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> userService.deleteUser(externalId));
        verify(accessService, times(1)).checkUser(externalId);
    }

    @Test
    void hardDeleteUserTest_success() {
        when(userRepository.existsByExternalId(externalId)).thenReturn(true);

        userService.hardDeleteUser(externalId);

        verify(accessService, times(1)).checkUser(externalId);
        verify(userRepository, times(1)).deleteByExternalId(externalId);
    }

    @Test
    void hardDeleteUserTest_userNotFound() {
        when(userRepository.existsByExternalId(externalId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                () -> userService.hardDeleteUser(externalId));
        verify(accessService, times(1)).checkUser(externalId);
    }

    @Test
    void updateUserStatusTest_success() {
        when(userRepository.findByExternalId(externalId)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toResponseDto(user)).thenReturn(userResponseDto);

        UserResponseDto response = userService.updateUserStatus(externalId, statusUpdateDto);

        assertNotNull(response);
        assertEquals(Status.ACTIVE, user.getStatus());
        verify(accessService, times(1)).checkUser(externalId);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void updateUserStatusTest_userNotFound() {
        when(userRepository.findByExternalId(externalId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> userService.updateUserStatus(externalId, statusUpdateDto));
        verify(accessService, times(1)).checkUser(externalId);
    }

    @Test
    void getUserEntityByExternalIdTest_success() {
        when(userRepository.findByExternalId(externalId)).thenReturn(Optional.of(user));

        UserEntity found = userService.getUserEntityByExternalId(externalId);

        assertNotNull(found);
        assertEquals(externalId, found.getExternalId());
        verify(userRepository, times(1)).findByExternalId(externalId);
    }

    @Test
    void getUserEntityByExternalIdTest_notFound() {
        when(userRepository.findByExternalId(externalId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> userService.getUserEntityByExternalId(externalId));
    }
}
