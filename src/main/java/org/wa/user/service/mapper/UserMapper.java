package org.wa.user.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.wa.user.service.dto.user.UserCreateDto;
import org.wa.user.service.dto.user.UserResponseDto;
import org.wa.user.service.dto.user.UserShortInfoDto;
import org.wa.user.service.dto.user.UserUpdateDto;
import org.wa.user.service.model.User;
import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {
    UserResponseDto toResponseDto(User user);

    List<UserShortInfoDto> toShortInfoDtoList(List<User> users);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifyAt", ignore = true)
    @Mapping(target = "userProfile", ignore = true)
    @Mapping(target = "connectedDevices", ignore = true)
    User toEntity(UserCreateDto createDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "birthday", ignore = true)
    @Mapping(target = "gender", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifyAt", ignore = true)
    @Mapping(target = "userProfile", ignore = true)
    @Mapping(target = "connectedDevices", ignore = true)
    void updateEntityFromDto(UserUpdateDto updateDto, @MappingTarget User user);
}
