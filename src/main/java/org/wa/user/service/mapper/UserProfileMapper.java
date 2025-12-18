package org.wa.user.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.wa.user.service.dto.profile.UserProfileRequestDto;
import org.wa.user.service.dto.profile.UserProfileResponseDto;
import org.wa.user.service.entity.UserProfileEntity;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {
    UserProfileResponseDto toResponseDto(UserProfileEntity profile);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    UserProfileEntity toEntity(UserProfileRequestDto requestDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    void updateEntityFromDto(UserProfileRequestDto requestDto, @MappingTarget UserProfileEntity profile);
}
