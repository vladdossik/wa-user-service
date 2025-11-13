package org.wa.user.service.util;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.wa.user.service.dto.device.ConnectedDeviceCreateDto;
import org.wa.user.service.dto.device.ConnectedDeviceResponseDto;
import org.wa.user.service.dto.device.ConnectedDeviceUpdateDto;
import org.wa.user.service.dto.profile.UserProfileRequestDto;
import org.wa.user.service.dto.profile.UserProfileResponseDto;
import org.wa.user.service.dto.user.UserCreateDto;
import org.wa.user.service.dto.user.UserResponseDto;
import org.wa.user.service.dto.user.UserShortInfoDto;
import org.wa.user.service.dto.user.UserStatusUpdateDto;
import org.wa.user.service.dto.user.UserUpdateDto;
import org.wa.user.service.entity.ConnectedDeviceEntity;
import org.wa.user.service.entity.UserEntity;
import org.wa.user.service.entity.UserProfileEntity;
import org.wa.user.service.entity.enumeration.ActivityLevel;
import org.wa.user.service.entity.enumeration.ConnectedDeviceType;
import org.wa.user.service.entity.enumeration.Gender;
import org.wa.user.service.entity.enumeration.HealthGoal;
import org.wa.user.service.entity.enumeration.Status;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

public class Initializer {

    public static final Long TEST_ID = 1L;
    public static final String TEST_EMAIL = "test@email.com";
    public static final String TEST_PHONE = "+79164538676";
    public static final String TEST_DEVICE_ID = "device-123";
    public static final String TEST_FIRST_NAME = "John";
    public static final String TEST_LAST_NAME = "Smith";
    public static final String TEST_DEVICE_MODEL = "Model X";
    public static final ConnectedDeviceType TEST_DEVICE_TYPE = ConnectedDeviceType.GOOGLE;
    public static final LocalDateTime TEST_BIRTHDAY = LocalDateTime.now().minusYears(20);
    public static final Gender TEST_GENDER = Gender.MALE;
    public static final Integer TEST_HEIGHT = 180;
    public static final Integer TEST_WEIGHT = 75;

    public static UserEntity createTestUser() {
        UserEntity user = new UserEntity();
        user.setId(TEST_ID);
        user.setEmail(TEST_EMAIL);
        user.setPhone(TEST_PHONE);
        user.setBirthday(TEST_BIRTHDAY);
        user.setGender(TEST_GENDER);
        user.setHeight(TEST_HEIGHT);
        user.setWeight(TEST_WEIGHT);
        user.setStatus(Status.ACTIVE);
        user.setCreatedAt(OffsetDateTime.now(ZoneOffset.UTC));
        user.setModifiedAt(OffsetDateTime.now(ZoneOffset.UTC));
        return user;
    }

    public static UserEntity createDeletedUser() {
        UserEntity user = createTestUser();
        user.setStatus(Status.DELETED);
        return user;
    }

    public static UserProfileEntity createTestUserProfile(UserEntity user) {
        UserProfileEntity profile = new UserProfileEntity();
        profile.setId(TEST_ID);
        profile.setUser(user);
        profile.setFirstName(TEST_FIRST_NAME);
        profile.setLastName(TEST_LAST_NAME);
        profile.setActivityLevel(ActivityLevel.MEDIUM);
        profile.setHealthGoal(HealthGoal.WEIGHT_LOSS);
        return profile;
    }

    public static ConnectedDeviceEntity createTestDevice(UserEntity user) {
        ConnectedDeviceEntity device = new ConnectedDeviceEntity();
        device.setId(TEST_ID);
        device.setUser(user);
        device.setDeviceType(TEST_DEVICE_TYPE);
        device.setDeviceId(TEST_DEVICE_ID);
        device.setDeviceModel(TEST_DEVICE_MODEL);
        device.setIsActive(true);
        device.setLastSyncAt(OffsetDateTime.now(ZoneOffset.UTC));
        return device;
    }

    public static UserCreateDto createTestUserCreateDto() {
        UserCreateDto dto = new UserCreateDto();
        dto.setEmail(TEST_EMAIL);
        dto.setPhone(TEST_PHONE);
        dto.setBirthday(TEST_BIRTHDAY);
        dto.setGender(TEST_GENDER);
        dto.setHeight(TEST_HEIGHT);
        dto.setWeight(TEST_WEIGHT);
        return dto;
    }

    public static UserUpdateDto createTestUserUpdateDto() {
        UserUpdateDto dto = new UserUpdateDto();
        dto.setEmail(TEST_EMAIL);
        dto.setPhone(TEST_PHONE);
        dto.setHeight(TEST_HEIGHT);
        dto.setWeight(TEST_WEIGHT);
        return dto;
    }

    public static UserUpdateDto createTestUserUpdateWithNewValues() {
        UserUpdateDto dto = new UserUpdateDto();
        dto.setEmail("new@email.com");
        dto.setPhone("+79164538677");
        dto.setHeight(185);
        dto.setWeight(80);
        return dto;
    }

    public static UserUpdateDto createTestUserUpdateWithExistingEmail() {
        UserUpdateDto dto = new UserUpdateDto();
        dto.setEmail("existing@email.com");
        dto.setPhone(Initializer.TEST_PHONE);
        dto.setHeight(Initializer.TEST_HEIGHT);
        dto.setWeight(Initializer.TEST_WEIGHT);
        return dto;
    }

    public static UserResponseDto createTestUserResponseDto() {
        UserResponseDto dto = new UserResponseDto();
        dto.setId(TEST_ID);
        dto.setEmail(TEST_EMAIL);
        dto.setPhone(TEST_PHONE);
        dto.setBirthday(TEST_BIRTHDAY);
        dto.setGender(TEST_GENDER);
        dto.setHeight(TEST_HEIGHT);
        dto.setWeight(TEST_WEIGHT);
        dto.setStatus(Status.ACTIVE);
        dto.setCreatedAt(OffsetDateTime.now(ZoneOffset.UTC));
        dto.setModifiedAt(OffsetDateTime.now(ZoneOffset.UTC));
        return dto;
    }

    public static UserShortInfoDto createTestUserShortInfoDto() {
        UserShortInfoDto dto = new UserShortInfoDto();
        dto.setId(TEST_ID);
        dto.setEmail(TEST_EMAIL);
        dto.setPhone(TEST_PHONE);
        dto.setStatus(Status.ACTIVE);
        return dto;
    }

    public static UserStatusUpdateDto createTestUserStatusUpdateDto() {
        UserStatusUpdateDto dto = new UserStatusUpdateDto();
        dto.setStatus(Status.ACTIVE);
        return dto;
    }

    public static UserProfileRequestDto createTestUserProfileRequestDto() {
        UserProfileRequestDto dto = new UserProfileRequestDto();
        dto.setFirstName(TEST_FIRST_NAME);
        dto.setLastName(TEST_LAST_NAME);
        dto.setActivityLevel(ActivityLevel.MEDIUM);
        dto.setHealthGoal(HealthGoal.WEIGHT_LOSS);
        return dto;
    }

    public static UserProfileResponseDto createTestUserProfileResponseDto() {
        UserProfileResponseDto dto = new UserProfileResponseDto();
        dto.setId(TEST_ID);
        dto.setFirstName(TEST_FIRST_NAME);
        dto.setLastName(TEST_LAST_NAME);
        dto.setActivityLevel(ActivityLevel.MEDIUM);
        dto.setHealthGoal(HealthGoal.WEIGHT_LOSS);
        return dto;
    }

    public static ConnectedDeviceCreateDto createTestDeviceCreateDto() {
        ConnectedDeviceCreateDto dto = new ConnectedDeviceCreateDto();
        dto.setDeviceId(TEST_DEVICE_ID);
        dto.setDeviceType(TEST_DEVICE_TYPE);
        dto.setDeviceModel(TEST_DEVICE_MODEL);
        return dto;
    }

    public static ConnectedDeviceUpdateDto createTestDeviceUpdateDto() {
        ConnectedDeviceUpdateDto dto = new ConnectedDeviceUpdateDto();
        dto.setDeviceModel(TEST_DEVICE_MODEL);
        dto.setIsActive(true);
        return dto;
    }

    public static ConnectedDeviceResponseDto createTestDeviceResponseDto() {
        ConnectedDeviceResponseDto dto = new ConnectedDeviceResponseDto();
        dto.setId(TEST_ID);
        dto.setDeviceType(TEST_DEVICE_TYPE);
        dto.setDeviceId(TEST_DEVICE_ID);
        dto.setDeviceModel(TEST_DEVICE_MODEL);
        dto.setIsActive(true);
        dto.setLastSyncAt(OffsetDateTime.now(ZoneOffset.UTC));
        return dto;
    }

    public static Pageable createDefaultPageable() {
        return PageRequest.of(0, 10);
    }

    public static Page<UserEntity> createUserPage(UserEntity user) {
        return new PageImpl<>(List.of(user));
    }
}
