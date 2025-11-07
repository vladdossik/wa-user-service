package org.wa.user.service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public ResponseEntity<List<ConnectedDeviceResponseDto>> getUserDevices(@PathVariable Long userId) {
        List<ConnectedDeviceResponseDto> devices = devicesService.getUserDevices(userId);
        return ResponseEntity.ok(devices);
    }

    @PostMapping
    public ResponseEntity<ConnectedDeviceResponseDto> addDevice(
            @PathVariable Long userId,
            @Valid @RequestBody ConnectedDeviceCreateDto deviceDto) {
        ConnectedDeviceResponseDto device = devicesService.addUserDevice(userId, deviceDto);
        return new ResponseEntity<>(device, HttpStatus.CREATED);
    }

    @PutMapping("/{deviceId}")
    public ResponseEntity<ConnectedDeviceResponseDto> updateDevice(
            @PathVariable Long userId,
            @PathVariable Long deviceId,
            @Valid @RequestBody ConnectedDeviceUpdateDto updateDto) {
        ConnectedDeviceResponseDto device = devicesService.updateUserDevice(userId, deviceId, updateDto);
        return ResponseEntity.ok(device);
    }

    @PatchMapping("/{deviceId}/sync")
    public ResponseEntity<ConnectedDeviceResponseDto> syncDevice(
            @PathVariable Long userId,
            @PathVariable Long deviceId) {
        ConnectedDeviceResponseDto device = devicesService.syncDevice(userId, deviceId);
        return ResponseEntity.ok(device);
    }

    @DeleteMapping("/{deviceId}")
    public ResponseEntity<Void> deleteDevice(@PathVariable Long userId,
                                             @PathVariable Long deviceId) {
        devicesService.deleteDevice(userId, deviceId);
        return ResponseEntity.noContent().build();
    }
}
