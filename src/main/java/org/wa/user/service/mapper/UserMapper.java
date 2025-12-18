package org.wa.user.service.mapper;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import org.wa.user.service.dto.user.UserCreateDto;
import org.wa.user.service.dto.user.UserRegisteredDto;
import org.wa.user.service.dto.user.UserResponseDto;
import org.wa.user.service.dto.user.UserShortInfoDto;
import org.wa.user.service.dto.user.UserUpdateDto;
import org.wa.user.service.entity.UserEntity;
import org.wa.user.service.service.DecryptService;

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

    @Mapping(target = "email", expression = "java(decryptService.decrypt(userRegisteredDto.getEmail()))")
    @Mapping(target = "phone", expression = "java(decryptService.decrypt(userRegisteredDto.getPhone()))")
    @Mapping(target = "birthday", ignore = true)
    @Mapping(target = "gender", ignore = true)
    @Mapping(target = "height", ignore = true)
    @Mapping(target = "weight", ignore = true)
    UserCreateDto toCreateDtoFromTopic(UserRegisteredDto userRegisteredDto, @Context DecryptService decryptService);

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
