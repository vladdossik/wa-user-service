package org.wa.user.service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.wa.user.service.dto.device.ConnectedDeviceCreateDto;
import org.wa.user.service.dto.device.ConnectedDeviceResponseDto;
import org.wa.user.service.dto.device.ConnectedDeviceUpdateDto;
import org.wa.user.service.service.ConnectedDeviceService;
import java.util.List;

@RestController
@RequestMapping("/v1/users/{userId}/devices")
@RequiredArgsConstructor
public class ConnectedDeviceController {
    private final ConnectedDeviceService devicesService;

    @GetMapping
    public List<ConnectedDeviceResponseDto> getUserDevices(@PathVariable Long userId) {
        return devicesService.getUserDevices(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ConnectedDeviceResponseDto addDevice(
            @PathVariable Long userId,
            @Valid @RequestBody ConnectedDeviceCreateDto deviceDto) {
        return devicesService.addUserDevice(userId, deviceDto);
    }

    @PutMapping("/{deviceId}")
    public ConnectedDeviceResponseDto updateDevice(
            @PathVariable Long userId,
            @PathVariable Long deviceId,
            @Valid @RequestBody ConnectedDeviceUpdateDto updateDto) {
        return devicesService.updateUserDevice(userId, deviceId, updateDto);
    }

    @PatchMapping("/{deviceId}/sync")
    public ConnectedDeviceResponseDto syncDevice(
            @PathVariable Long userId,
            @PathVariable Long deviceId) {
        return devicesService.syncDevice(userId, deviceId);
    }

    @DeleteMapping("/{deviceId}")
    public void deleteDevice(@PathVariable Long userId,
                                             @PathVariable Long deviceId) {
        devicesService.deleteDevice(userId, deviceId);
    }
}
