package org.wa.user.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.wa.user.service.dto.connectedDevices.ConnectedDeviceCreateDto;
import org.wa.user.service.dto.connectedDevices.ConnectedDeviceResponseDto;
import org.wa.user.service.dto.connectedDevices.ConnectedDeviceUpdateDto;
import org.wa.user.service.model.ConnectedDevices;
import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ConnectedDevicesMapper {
    ConnectedDeviceResponseDto toResponseDto(ConnectedDevices device);

    List<ConnectedDeviceResponseDto> toResponseDtoList(List<ConnectedDevices> devices);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "lastSyncAt", ignore = true)
    ConnectedDevices toEntity(ConnectedDeviceCreateDto createDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "deviceType", ignore = true)
    @Mapping(target = "deviceId", ignore = true)
    @Mapping(target = "lastSyncAt", ignore = true)
    void updateEntityFromDto(ConnectedDeviceUpdateDto updateDto, @MappingTarget ConnectedDevices device);

}
