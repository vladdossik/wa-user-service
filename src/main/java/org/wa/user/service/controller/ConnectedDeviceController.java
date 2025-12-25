package org.wa.user.service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Devices", description = "API для управления устройствами пользователей")
@SecurityRequirement(name = "bearerAuth")
public class ConnectedDeviceController {
    private final ConnectedDeviceService devicesService;

    @GetMapping
    @Operation(summary = "Получить устройства пользователя")
    public List<ConnectedDeviceResponseDto> getUserDevices(@PathVariable("userId") Long userId) {
        return devicesService.getUserDevices(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Добавить устройство")
    public ConnectedDeviceResponseDto addDevice(
            @PathVariable("userId") Long userId,
            @Valid @RequestBody ConnectedDeviceCreateDto deviceDto) {
        return devicesService.addUserDevice(userId, deviceDto);
    }

    @PutMapping("/{deviceId}")
    @Operation(summary = "Обновить устройство")
    public ConnectedDeviceResponseDto updateDevice(
            @PathVariable("userId") Long userId,
            @PathVariable("deviceId") Long deviceId,
            @Valid @RequestBody ConnectedDeviceUpdateDto updateDto) {
        return devicesService.updateUserDevice(userId, deviceId, updateDto);
    }

    @PatchMapping("/{deviceId}/sync")
    @Operation(summary = "Синхронизировать устройство")
    public ConnectedDeviceResponseDto syncDevice(
            @PathVariable("userId") Long userId,
            @PathVariable("deviceId") Long deviceId) {
        return devicesService.syncDevice(userId, deviceId);
    }

    @DeleteMapping("/{deviceId}")
    @Operation(summary = "Удалить устройство")
    public void deleteDevice(@PathVariable("userId") Long userId,
                                             @PathVariable("deviceId") Long deviceId) {
        devicesService.deleteDevice(userId, deviceId);
    }
}
