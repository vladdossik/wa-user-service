package org.wa.user.service.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wa.user.service.dto.device.ConnectedDeviceCreateDto;
import org.wa.user.service.dto.device.ConnectedDeviceResponseDto;
import org.wa.user.service.dto.device.ConnectedDeviceUpdateDto;
import org.wa.user.service.exception.AttributeDuplicateException;
import org.wa.user.service.exception.ResourceNotFoundException;
import org.wa.user.service.mapper.ConnectedDeviceMapper;
import org.wa.user.service.model.ConnectedDevice;
import org.wa.user.service.repository.ConnectedDeviceRepository;
import org.wa.user.service.service.ConnectedDeviceService;
import org.wa.user.service.service.UserService;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConnectedDeviceServiceImpl implements ConnectedDeviceService {
    private final ConnectedDeviceRepository deviceRepository;
    private final UserService userService;
    private final ConnectedDeviceMapper deviceMapper;

    @Override
    public List<ConnectedDeviceResponseDto> getUserDevices(Long userId) {
        if (!userService.userExists(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }

        return deviceMapper.toResponseDtoList(deviceRepository.findByUserId(userId));
    }

    @Override
    @Transactional
    public ConnectedDeviceResponseDto addUserDevice(Long id, ConnectedDeviceCreateDto deviceCreateDto) {
        var user = userService.getUserEntity(id);

        if (deviceRepository.existsByDeviceId(deviceCreateDto.getDeviceId())) {
            throw new AttributeDuplicateException("Device ID already exists: " + deviceCreateDto.getDeviceId());
        }

        ConnectedDevice device = deviceMapper.toEntity(deviceCreateDto);
        device.setUser(user);
        device.setIsActive(true);
        device.setLastSyncAt(OffsetDateTime.now(ZoneOffset.UTC));

        ConnectedDevice savedDevice = deviceRepository.save(device);
        return deviceMapper.toResponseDto(savedDevice);
    }

    @Override
    @Transactional
    public ConnectedDeviceResponseDto updateUserDevice(Long userId, Long deviceId,
                                                       ConnectedDeviceUpdateDto deviceUpdateDto) {
        if (!userService.userExists(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }

        ConnectedDevice device = deviceRepository.findByUserIdAndDeviceId(userId, deviceId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Device not found with id: %d for user id: %d", deviceId, userId)
                ));

        deviceMapper.updateEntityFromDto(deviceUpdateDto, device);
        ConnectedDevice updatedDevice = deviceRepository.save(device);
        return deviceMapper.toResponseDto(updatedDevice);
    }

    @Override
    @Transactional
    public void deleteDevice(Long userId, Long deviceId) {
        ConnectedDevice device = deviceRepository.findByUserIdAndDeviceId(userId, deviceId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Device not found with id: %d for user id: %d", deviceId, userId)
                ));

        deviceRepository.delete(device);
    }

    @Override
    @Transactional
    public ConnectedDeviceResponseDto syncDevice(Long userId, Long deviceId) {
        ConnectedDevice device = deviceRepository.findByUserIdAndDeviceId(userId, deviceId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Device not found with id: %d for user id: %d", deviceId, userId)
                ));

        device.setLastSyncAt(OffsetDateTime.now(ZoneOffset.UTC));
        ConnectedDevice updatedDevice = deviceRepository.save(device);
        return deviceMapper.toResponseDto(updatedDevice);
    }
}
