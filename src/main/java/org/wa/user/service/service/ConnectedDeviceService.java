package org.wa.user.service.service;

import org.wa.user.service.dto.device.ConnectedDeviceCreateDto;
import org.wa.user.service.dto.device.ConnectedDeviceResponseDto;
import org.wa.user.service.dto.device.ConnectedDeviceUpdateDto;
import java.util.List;

public interface ConnectedDeviceService {
    List<ConnectedDeviceResponseDto> getUserDevices(Long id);

    ConnectedDeviceResponseDto addUserDevice(Long id, ConnectedDeviceCreateDto deviceCreateDto);

    ConnectedDeviceResponseDto updateUserDevice(Long userId, Long deviceId, ConnectedDeviceUpdateDto deviceUpdateDto);

    void deleteDevice(Long userId, Long deviceId);

    ConnectedDeviceResponseDto syncDevice(Long userId, Long deviceId);
}
