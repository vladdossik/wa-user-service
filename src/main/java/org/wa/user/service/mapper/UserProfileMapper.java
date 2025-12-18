package org.wa.user.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.wa.user.service.dto.profile.UserProfileRequestDto;
import org.wa.user.service.dto.profile.UserProfileResponseDto;
import org.wa.user.service.dto.user.UserResponseDto;
import org.wa.user.service.entity.UserProfileEntity;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {
    UserProfileResponseDto toResponseDto(UserProfileEntity profile);

    @Mapping(target = "firstName", expression = "java(buildFirstName(userResponseDto.getId()))")
    @Mapping(target = "lastName", expression = "java(extractLastName(userResponseDto.getEmail()))")
    @Mapping(target = "activityLevel", ignore = true)
    @Mapping(target = "healthGoal", ignore = true)
    UserProfileRequestDto toRequestDto(UserResponseDto userResponseDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    UserProfileEntity toEntity(UserProfileRequestDto requestDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    void updateEntityFromDto(UserProfileRequestDto requestDto, @MappingTarget UserProfileEntity profile);

    default String buildFirstName(Long userId) {
        return userId == null ? "user" : "user" + userId;
    }

    default String extractLastName(String email) {
        if (email == null || email.isBlank()) {
            return "no email user";
        }
        int atIndex = email.indexOf("@");
        return atIndex > 0 ? email.substring(0, atIndex) : email;
    }
}
