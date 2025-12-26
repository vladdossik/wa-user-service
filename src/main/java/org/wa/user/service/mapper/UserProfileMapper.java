package org.wa.user.service.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.wa.user.service.dto.profile.UserProfileRequestDto;
import org.wa.user.service.dto.profile.UserProfileResponseDto;
import org.wa.user.service.dto.user.UserResponseDto;
import org.wa.user.service.entity.UserProfileEntity;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {
    UserProfileResponseDto toResponseDto(UserProfileEntity profile);

    @Mapping(target = "firstName", expression = "java(buildFirstName(userResponseDto.getId()))")
    @Mapping(target = "lastName", ignore = true)
    @Mapping(target = "activityLevel", ignore = true)
    @Mapping(target = "healthGoal", ignore = true)
    UserProfileRequestDto toRequestDto(UserResponseDto userResponseDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    UserProfileEntity toEntity(UserProfileRequestDto requestDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    void updateEntityFromDto(UserProfileRequestDto requestDto, @MappingTarget UserProfileEntity profile);

    default String buildFirstName(Long userId) {
        return userId == null ? "user" : "user" + userId;
    }

}
