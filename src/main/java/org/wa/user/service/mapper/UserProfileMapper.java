package org.wa.user.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.wa.user.service.dto.userProfile.UserProfileRequestDto;
import org.wa.user.service.dto.userProfile.UserProfileResponseDto;
import org.wa.user.service.model.UserProfile;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserProfileMapper {
    UserProfileResponseDto toResponseDto(UserProfile profile);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    UserProfile toEntity(UserProfileRequestDto requestDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    void updateEntityFromDto(UserProfileRequestDto requestDto, @MappingTarget UserProfile profile);
}
