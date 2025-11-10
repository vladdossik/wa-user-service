package org.wa.user.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.wa.user.service.dto.device.ConnectedDeviceCreateDto;
import org.wa.user.service.dto.device.ConnectedDeviceResponseDto;
import org.wa.user.service.dto.device.ConnectedDeviceUpdateDto;
import org.wa.user.service.entity.ConnectedDeviceEntity;
import org.wa.user.service.entity.UserEntity;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ConnectedDeviceMapper {
    ConnectedDeviceResponseDto toResponseDto(ConnectedDeviceEntity device);

    List<ConnectedDeviceResponseDto> toResponseDtoList(List<ConnectedDeviceEntity> devices);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "lastSyncAt", ignore = true)
    ConnectedDeviceEntity toEntity(ConnectedDeviceCreateDto createDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "deviceType", ignore = true)
    @Mapping(target = "deviceId", ignore = true)
    @Mapping(target = "lastSyncAt", ignore = true)
    void updateEntityFromDto(ConnectedDeviceUpdateDto updateDto, @MappingTarget ConnectedDeviceEntity device);

    @Named("setDeviceDefaults")
    default ConnectedDeviceEntity setDeviceDefaults(ConnectedDeviceCreateDto createDto, UserEntity userEntity) {
        ConnectedDeviceEntity device = toEntity(createDto);
        device.setUser(userEntity);
        device.setIsActive(true);
        device.setLastSyncAt(OffsetDateTime.now(ZoneOffset.UTC));
        return device;
    }
}
