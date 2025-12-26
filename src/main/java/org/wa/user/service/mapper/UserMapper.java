package org.wa.user.service.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.beans.factory.annotation.Autowired;

import org.wa.user.service.dto.user.UserCreateDto;
import org.wa.user.service.dto.user.UserRegisteredDto;
import org.wa.user.service.dto.user.UserResponseDto;
import org.wa.user.service.dto.user.UserShortInfoDto;
import org.wa.user.service.dto.user.UserUpdateDto;
import org.wa.user.service.entity.UserEntity;
import org.wa.user.service.service.DecryptService;

@Mapper(componentModel = "spring")
public abstract class UserMapper {
    
    @Autowired
    protected DecryptService decryptService;
    
    public abstract UserResponseDto toResponseDto(UserEntity userEntity);

    public abstract UserShortInfoDto toShortInfoDto(UserEntity userEntity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    @Mapping(target = "userProfile", ignore = true)
    @Mapping(target = "connectedDevices", ignore = true)
    public abstract UserEntity toEntity(UserCreateDto createDto);

    @Mapping(target = "email", expression = "java(decryptService.decrypt(userRegisteredDto.getEmail()))")
    @Mapping(target = "phone", expression = "java(decryptService.decrypt(userRegisteredDto.getPhone()))")
    @Mapping(target = "birthday", ignore = true)
    @Mapping(target = "gender", ignore = true)
    @Mapping(target = "height", ignore = true)
    @Mapping(target = "weight", ignore = true)
    public abstract UserCreateDto toCreateDtoFromTopic(UserRegisteredDto userRegisteredDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "birthday", ignore = true)
    @Mapping(target = "gender", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    @Mapping(target = "userProfile", ignore = true)
    @Mapping(target = "connectedDevices", ignore = true)
    public abstract void updateEntityFromDto(UserUpdateDto updateDto, @MappingTarget UserEntity userEntity);
}
