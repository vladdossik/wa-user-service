package org.wa.user.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.wa.user.service.dto.user.UserCreateDto;
import org.wa.user.service.dto.user.UserResponseDto;
import org.wa.user.service.dto.user.UserShortInfoDto;
import org.wa.user.service.dto.user.UserUpdateDto;
import org.wa.user.service.entity.UserEntity;
import org.wa.user.service.util.Initializer;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mapstruct.factory.Mappers.getMapper;

public class UserMapperTest {
    private final UserMapper userMapper = getMapper(UserMapper.class);

    private UserEntity user;
    private UserCreateDto createDto;
    private UserUpdateDto updateDto;

    @BeforeEach
    void setUp() {
        user = Initializer.createTestUser();
        createDto = Initializer.createTestUserCreateDto();
        updateDto = Initializer.createTestUserUpdateDto();
    }

    @Test
    void toResponseDtoTest() {
        UserResponseDto responseDto = userMapper.toResponseDto(user);

        assertNotNull(responseDto);
        assertEquals(user.getId(), responseDto.getId());
        assertEquals(user.getEmail(), responseDto.getEmail());
        assertEquals(user.getPhone(), responseDto.getPhone());
        assertEquals(user.getBirthday(), responseDto.getBirthday());
        assertEquals(user.getGender(), responseDto.getGender());
        assertEquals(user.getHeight(), responseDto.getHeight());
        assertEquals(user.getWeight(), responseDto.getWeight());
        assertEquals(user.getStatus(), responseDto.getStatus());
        assertEquals(user.getCreatedAt(), responseDto.getCreatedAt());
        assertEquals(user.getModifiedAt(), responseDto.getModifiedAt());
    }

    @Test
    void toResponseDtoNullTest() {
        assertNull(userMapper.toResponseDto(null));
    }

    @Test
    void toShortInfoDtoTest() {
        UserShortInfoDto shortInfoDto = userMapper.toShortInfoDto(user);

        assertNotNull(shortInfoDto);
        assertEquals(user.getId(), shortInfoDto.getId());
        assertEquals(user.getEmail(), shortInfoDto.getEmail());
        assertEquals(user.getPhone(), shortInfoDto.getPhone());
        assertEquals(user.getStatus(), shortInfoDto.getStatus());
    }

    @Test
    void toShortInfoDtoNullTest() {
        assertNull(userMapper.toShortInfoDto(null));
    }

    @Test
    void toEntityTest() {
        UserEntity entity = userMapper.toEntity(createDto);

        assertNotNull(entity);
        assertNull(entity.getId());
        assertNull(entity.getStatus());
        assertNull(entity.getCreatedAt());
        assertNull(entity.getModifiedAt());
        assertNull(entity.getUserProfile());
        assertNotNull(entity.getConnectedDevices());
        assertTrue(entity.getConnectedDevices().isEmpty());
        assertEquals(createDto.getEmail(), entity.getEmail());
        assertEquals(createDto.getPhone(), entity.getPhone());
        assertEquals(createDto.getBirthday(), entity.getBirthday());
        assertEquals(createDto.getGender(), entity.getGender());
        assertEquals(createDto.getHeight(), entity.getHeight());
        assertEquals(createDto.getWeight(), entity.getWeight());
    }

    @Test
    void toEntityNullTest() {
        assertNull(userMapper.toEntity(null));
    }

    @Test
    void updateEntityFromDtoTest() {
        UserEntity existingUser = Initializer.createTestUser();
        existingUser.setEmail("old@email.com");
        existingUser.setPhone("+79160000000");
        existingUser.setHeight(170);
        existingUser.setWeight(70);

        userMapper.updateEntityFromDto(updateDto, existingUser);

        assertEquals(updateDto.getEmail(), existingUser.getEmail());
        assertEquals(updateDto.getPhone(), existingUser.getPhone());
        assertEquals(updateDto.getHeight(), existingUser.getHeight());
        assertEquals(updateDto.getWeight(), existingUser.getWeight());
        assertNotNull(existingUser.getBirthday());
        assertNotNull(existingUser.getGender());
        assertNotNull(existingUser.getStatus());
        assertNotNull(existingUser.getCreatedAt());
        assertNotNull(existingUser.getModifiedAt());
    }

    @Test
    void updateEntityFromDtoPartialTest() {
        UserEntity existingUser = Initializer.createTestUser();
        String originalPhone = existingUser.getPhone();
        Integer originalWeight = existingUser.getWeight();

        UserUpdateDto partialUpdateDto = new UserUpdateDto();
        partialUpdateDto.setEmail("updated@email.com");
        partialUpdateDto.setHeight(185);

        userMapper.updateEntityFromDto(partialUpdateDto, existingUser);

        assertEquals("updated@email.com", existingUser.getEmail());
        assertEquals(185, existingUser.getHeight());
        assertEquals(originalPhone, existingUser.getPhone());
        assertEquals(originalWeight, existingUser.getWeight());
    }

    @Test
    void updateEntityFromDtoNullTest() {
        UserEntity existingUser = Initializer.createTestUser();
        String originalEmail = existingUser.getEmail();
        String originalPhone = existingUser.getPhone();

        userMapper.updateEntityFromDto(null, existingUser);

        assertEquals(originalEmail, existingUser.getEmail());
        assertEquals(originalPhone, existingUser.getPhone());
    }
}
