package org.wa.user.service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wa.user.service.config.UserAccessService;
import org.wa.user.service.dto.device.ConnectedDeviceCreateDto;
import org.wa.user.service.dto.device.ConnectedDeviceResponseDto;
import org.wa.user.service.dto.device.ConnectedDeviceUpdateDto;
import org.wa.user.service.exception.AttributeDuplicateException;
import org.wa.user.service.exception.ResourceNotFoundException;
import org.wa.user.service.mapper.ConnectedDeviceMapper;
import org.wa.user.service.entity.ConnectedDeviceEntity;
import org.wa.user.service.entity.UserEntity;
import org.wa.user.service.repository.ConnectedDeviceRepository;
import org.wa.user.service.service.ConnectedDeviceService;
import org.wa.user.service.service.UserService;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConnectedDeviceServiceImpl implements ConnectedDeviceService {
    private final ConnectedDeviceRepository deviceRepository;
    private final UserService userService;
    private final ConnectedDeviceMapper deviceMapper;
    private final UserAccessService accessService;

    @Override
    @Transactional(readOnly = true)
    public List<ConnectedDeviceResponseDto> getUserDevices(Long userId) {
        log.info("Getting devices for user id: {}", userId);
        accessService.checkUserAccess(userId);

        if (!userService.userExists(userId)) {
            log.warn("User not found when getting devices for user id: {}", userId);
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }

        List<ConnectedDeviceEntity> devices = deviceRepository.findByUserId(userId);
        List<ConnectedDeviceResponseDto> response = deviceMapper.toResponseDtoList(devices);
        log.info("Successfully retrieved {} devices for user id: {}", devices.size(), userId);

        return response;
    }

    @Override
    @Transactional
    public ConnectedDeviceResponseDto addUserDevice(Long id, ConnectedDeviceCreateDto deviceCreateDto) {
        log.info("Adding new device for user id: {}, device ID: {}", id, deviceCreateDto.getDeviceId());
        accessService.checkUserAccess(id);

        UserEntity userEntity = userService.getUserEntity(id);

        if (deviceRepository.existsByDeviceId(deviceCreateDto.getDeviceId())) {
            log.warn("Attempt to add duplicate device ID: {}", deviceCreateDto.getDeviceId());
            throw new AttributeDuplicateException("Device ID already exists: " + deviceCreateDto.getDeviceId());
        }

        log.debug("Mapping ConnectedDeviceCreateDto to ConnectedDeviceEntity with defaults values");
        ConnectedDeviceEntity device = deviceMapper.setDeviceDefaults(deviceCreateDto, userEntity);

        log.debug("Saving device to database");
        ConnectedDeviceEntity savedDevice = deviceRepository.save(device);
        ConnectedDeviceResponseDto response = deviceMapper.toResponseDto(savedDevice);
        log.info("Successfully added device with id: {} for user id: {}",
                savedDevice.getId(), id);

        return response;
    }

    @Override
    @Transactional
    public ConnectedDeviceResponseDto updateUserDevice(Long userId, Long deviceId,
                                                       ConnectedDeviceUpdateDto deviceUpdateDto) {
        log.info("Updating device with id: {} for user id: {}", deviceId, userId);
        accessService.checkUserAccess(userId);

        if (!userService.userExists(userId)) {
            log.warn("User not found when updating device for user id: {}", userId);
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }

        log.debug("Mapping ConnectedDeviceUpdateDto to existing ConnectedDeviceEntity");
        deviceMapper.updateEntityFromDto(deviceUpdateDto, getDevice(userId, deviceId));

        log.debug("Saving updated device to database");
        ConnectedDeviceEntity updatedDevice = deviceRepository.save(getDevice(userId, deviceId));
        ConnectedDeviceResponseDto response = deviceMapper.toResponseDto(updatedDevice);
        log.info("Successfully updated device with id: {} for user id: {}", deviceId, userId);

        return response;
    }

    @Override
    @Transactional
    public void deleteDevice(Long userId, Long deviceId) {
        log.info("Deleting device with id: {} for user id: {}", deviceId, userId);
        accessService.checkUserAccess(userId);

        deviceRepository.delete(getDevice(userId, deviceId));
        log.info("Successfully deleted device with id: {} for user id: {}", deviceId, userId);
    }

    @Override
    @Transactional
    public ConnectedDeviceResponseDto syncDevice(Long userId, Long deviceId) {
        log.info("Synchronization of device with id: {} for user id: {}", deviceId, userId);
        accessService.checkUserAccess(userId);

        ConnectedDeviceEntity device = getDevice(userId, deviceId);
        device.setLastSyncAt(OffsetDateTime.now(ZoneOffset.UTC));

        ConnectedDeviceEntity updatedDevice = deviceRepository.save(device);
        ConnectedDeviceResponseDto response = deviceMapper.toResponseDto(updatedDevice);
        log.info("Successfully synchronized of device with id: {} for user id: {}", deviceId, userId);

        return response;
    }

    private ConnectedDeviceEntity getDevice(Long userId, Long deviceId) {
        log.debug("Fetching device with id: {} for user id: {}", deviceId, userId);

        return deviceRepository.findByUserIdAndDeviceId(userId, deviceId)
                .orElseThrow(() -> {
                    log.warn("Device not found with id: {} for user id: {}", deviceId, userId);
                    return new ResourceNotFoundException(
                            String.format("Device not found with id: %d for user id: %d", deviceId, userId)
                    );
                });
    }
}
