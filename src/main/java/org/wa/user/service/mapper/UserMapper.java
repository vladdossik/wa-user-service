package org.wa.user.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import org.wa.user.service.dto.user.UserCreateDto;
import org.wa.user.service.dto.user.UserResponseDto;
import org.wa.user.service.dto.user.UserShortInfoDto;
import org.wa.user.service.dto.user.UserUpdateDto;
import org.wa.user.service.entity.UserEntity;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponseDto toResponseDto(UserEntity userEntity);

    UserShortInfoDto toShortInfoDto(UserEntity userEntity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    @Mapping(target = "userProfile", ignore = true)
    @Mapping(target = "connectedDevices", ignore = true)
    UserEntity toEntity(UserCreateDto createDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "birthday", ignore = true)
    @Mapping(target = "gender", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    @Mapping(target = "userProfile", ignore = true)
    @Mapping(target = "connectedDevices", ignore = true)
    void updateEntityFromDto(UserUpdateDto updateDto, @MappingTarget UserEntity userEntity);
}
