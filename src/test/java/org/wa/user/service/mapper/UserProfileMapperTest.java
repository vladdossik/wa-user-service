package org.wa.user.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.wa.user.service.dto.profile.UserProfileRequestDto;
import org.wa.user.service.dto.profile.UserProfileResponseDto;
import org.wa.user.service.entity.UserEntity;
import org.wa.user.service.entity.UserProfileEntity;
import org.wa.user.service.entity.enumeration.ActivityLevel;
import org.wa.user.service.entity.enumeration.HealthGoal;
import org.wa.user.service.util.Initializer;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mapstruct.factory.Mappers.getMapper;

public class UserProfileMapperTest {
    private final UserProfileMapper userProfileMapper = getMapper(UserProfileMapper.class);

    private UserProfileEntity userProfile;
    private UserProfileRequestDto requestDto;
    private UserEntity user;

    @BeforeEach
    void setUp() {
        user = Initializer.createTestUser();
        userProfile = Initializer.createTestUserProfile(user);
        requestDto = Initializer.createTestUserProfileRequestDto();
    }

    @Test
    void toResponseDtoTest() {
        UserProfileResponseDto responseDto = userProfileMapper.toResponseDto(userProfile);

        assertNotNull(responseDto);
        assertEquals(userProfile.getId(), responseDto.getId());
        assertEquals(userProfile.getFirstName(), responseDto.getFirstName());
        assertEquals(userProfile.getLastName(), responseDto.getLastName());
        assertEquals(userProfile.getActivityLevel(), responseDto.getActivityLevel());
        assertEquals(userProfile.getHealthGoal(), responseDto.getHealthGoal());
    }

    @Test
    void toResponseDtoNullTest() {
        assertNull(userProfileMapper.toResponseDto(null));
    }

    @Test
    void toEntityTest() {
        UserProfileEntity entity = userProfileMapper.toEntity(requestDto);

        assertNotNull(entity);
        assertNull(entity.getId());
        assertNull(entity.getUser());
        assertEquals(requestDto.getFirstName(), entity.getFirstName());
        assertEquals(requestDto.getLastName(), entity.getLastName());
        assertEquals(requestDto.getActivityLevel(), entity.getActivityLevel());
        assertEquals(requestDto.getHealthGoal(), entity.getHealthGoal());
    }

    @Test
    void toEntityNullTest() {
        assertNull(userProfileMapper.toEntity(null));
    }

    @Test
    void updateEntityFromDtoTest() {
        UserProfileEntity existingProfile = Initializer.createTestUserProfile(user);
        existingProfile.setFirstName("OldFirstName");
        existingProfile.setLastName("OldLastName");
        existingProfile.setActivityLevel(ActivityLevel.LOW);
        existingProfile.setHealthGoal(HealthGoal.WEIGHT_LOSS);

        userProfileMapper.updateEntityFromDto(requestDto, existingProfile);

        assertEquals(requestDto.getFirstName(), existingProfile.getFirstName());
        assertEquals(requestDto.getLastName(), existingProfile.getLastName());
        assertEquals(requestDto.getActivityLevel(), existingProfile.getActivityLevel());
        assertEquals(requestDto.getHealthGoal(), existingProfile.getHealthGoal());
        assertNotNull(existingProfile.getId());
        assertNotNull(existingProfile.getUser());
    }

    @Test
    void updateEntityFromDtoPartialTest() {
        UserProfileEntity existingProfile = Initializer.createTestUserProfile(user);
        String originalLastName = existingProfile.getLastName();
        org.wa.user.service.entity.enumeration.HealthGoal originalHealthGoal = existingProfile.getHealthGoal();

        UserProfileRequestDto partialRequestDto = new UserProfileRequestDto();
        partialRequestDto.setFirstName("UpdatedFirstName");
        partialRequestDto.setActivityLevel(org.wa.user.service.entity.enumeration.ActivityLevel.HIGH);

        userProfileMapper.updateEntityFromDto(partialRequestDto, existingProfile);

        assertEquals("UpdatedFirstName", existingProfile.getFirstName());
        assertEquals(org.wa.user.service.entity.enumeration.ActivityLevel.HIGH, existingProfile.getActivityLevel());
        assertEquals(originalLastName, existingProfile.getLastName());
        assertEquals(originalHealthGoal, existingProfile.getHealthGoal());
    }

    @Test
    void updateEntityFromDtoNullTest() {
        UserProfileEntity existingProfile = Initializer.createTestUserProfile(user);
        String originalFirstName = existingProfile.getFirstName();
        String originalLastName = existingProfile.getLastName();

        userProfileMapper.updateEntityFromDto(null, existingProfile);

        assertEquals(originalFirstName, existingProfile.getFirstName());
        assertEquals(originalLastName, existingProfile.getLastName());
    }
}
