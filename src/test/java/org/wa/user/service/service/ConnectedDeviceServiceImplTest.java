package org.wa.user.service.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.wa.user.service.dto.device.ConnectedDeviceCreateDto;
import org.wa.user.service.dto.device.ConnectedDeviceResponseDto;
import org.wa.user.service.dto.device.ConnectedDeviceUpdateDto;
import org.wa.user.service.entity.ConnectedDeviceEntity;
import org.wa.user.service.entity.UserEntity;
import org.wa.user.service.exception.AttributeDuplicateException;
import org.wa.user.service.exception.ResourceNotFoundException;
import org.wa.user.service.mapper.ConnectedDeviceMapper;
import org.wa.user.service.repository.ConnectedDeviceRepository;
import org.wa.user.service.repository.UserRepository;
import org.wa.user.service.service.impl.ConnectedDeviceServiceImpl;
import org.wa.user.service.util.Initializer;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ConnectedDeviceServiceImplTest {
    @Mock
    private ConnectedDeviceRepository deviceRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ConnectedDeviceMapper deviceMapper;

    @Mock
    private UserAccessService accessService;

    @InjectMocks
    private ConnectedDeviceServiceImpl connectedDeviceService;

    private UserEntity user;
    private ConnectedDeviceEntity device;
    private ConnectedDeviceCreateDto deviceCreateDto;
    private ConnectedDeviceUpdateDto deviceUpdateDto;
    private ConnectedDeviceResponseDto deviceResponseDto;
    private Long userId;

    @BeforeEach
    void setUp() {
        user = Initializer.createTestUser();
        userId = user.getId();
        device = Initializer.createTestDevice(user);
        deviceCreateDto = Initializer.createTestDeviceCreateDto();
        deviceUpdateDto = Initializer.createTestDeviceUpdateDto();
        deviceResponseDto = Initializer.createTestDeviceResponseDto();
    }

    @Test
    void getUserDevicesTest_success() {
        when(userRepository.existsById(userId)).thenReturn(true);
        when(deviceRepository.findByUserId(userId)).thenReturn(List.of(device));
        when(deviceMapper.toResponseDtoList(List.of(device))).thenReturn(List.of(deviceResponseDto));

        List<ConnectedDeviceResponseDto> response = connectedDeviceService.getUserDevices(userId);

        assertNotNull(response);
        assertEquals(List.of(deviceResponseDto), response);
        verify(accessService, times(1)).checkUser(userId);
        verify(deviceRepository, times(1)).findByUserId(userId);
    }

    @Test
    void getUserDevicesTest_userNotFound() {
        when(userRepository.existsById(userId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                () -> connectedDeviceService.getUserDevices(userId));
        verify(accessService, times(1)).checkUser(userId);
    }

    @Test
    void addUserDeviceTest_success() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(deviceRepository.existsByDeviceId(deviceCreateDto.getDeviceId())).thenReturn(false);
        when(deviceMapper.setDeviceDefaults(deviceCreateDto, user)).thenReturn(device);
        when(deviceRepository.save(device)).thenReturn(device);
        when(deviceMapper.toResponseDto(device)).thenReturn(deviceResponseDto);

        ConnectedDeviceResponseDto response = connectedDeviceService.addUserDevice(userId, deviceCreateDto);

        assertNotNull(response);
        verify(accessService, times(1)).checkUser(userId);
        verify(deviceMapper, times(1)).setDeviceDefaults(deviceCreateDto, user);
        verify(deviceRepository, times(1)).save(device);
    }

    @Test
    void addUserDeviceTest_deviceExists() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(deviceRepository.existsByDeviceId(deviceCreateDto.getDeviceId())).thenReturn(true);

        assertThrows(AttributeDuplicateException.class,
                () -> connectedDeviceService.addUserDevice(userId, deviceCreateDto));
        verify(accessService, times(1)).checkUser(userId);
    }

    @Test
    void addUserDeviceTest_userNotFound() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> connectedDeviceService.addUserDevice(userId, deviceCreateDto));
        verify(accessService, times(1)).checkUser(userId);
    }

    @Test
    void updateUserDeviceTest_success() {
        when(userRepository.existsById(userId)).thenReturn(true);
        when(deviceRepository.findByUserIdAndDeviceId(userId, 1L))
                .thenReturn(Optional.of(device));
        when(deviceRepository.save(device)).thenReturn(device);
        when(deviceMapper.toResponseDto(device)).thenReturn(deviceResponseDto);

        ConnectedDeviceResponseDto response = connectedDeviceService.updateUserDevice(
                userId, 1L, deviceUpdateDto);

        assertNotNull(response);
        verify(accessService, times(1)).checkUser(userId);
        verify(deviceMapper, times(1)).updateEntityFromDto(deviceUpdateDto, device);
        verify(deviceRepository, times(1)).save(device);
    }

    @Test
    void updateUserDeviceTest_userNotFound() {
        when(userRepository.existsById(userId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                () -> connectedDeviceService.updateUserDevice(userId, 1L, deviceUpdateDto));
        verify(accessService, times(1)).checkUser(userId);
    }

    @Test
    void updateUserDeviceTest_deviceNotFound() {
        when(userRepository.existsById(userId)).thenReturn(true);
        when(deviceRepository.findByUserIdAndDeviceId(userId, 1L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> connectedDeviceService.updateUserDevice(userId, 1L, deviceUpdateDto));
        verify(accessService, times(1)).checkUser(userId);
    }

    @Test
    void syncDeviceTest_success() {
        when(deviceRepository.findByUserIdAndDeviceId(userId, 1L))
                .thenReturn(Optional.of(device));
        when(deviceRepository.save(device)).thenReturn(device);
        when(deviceMapper.toResponseDto(device)).thenReturn(deviceResponseDto);

        ConnectedDeviceResponseDto response = connectedDeviceService.syncDevice(userId, 1L);

        assertNotNull(response);
        assertNotNull(device.getLastSyncAt());
        verify(accessService, times(1)).checkUser(userId);
        verify(deviceRepository, times(1)).save(device);
    }

    @Test
    void syncDeviceTest_deviceNotFound() {
        when(deviceRepository.findByUserIdAndDeviceId(userId, 1L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> connectedDeviceService.syncDevice(userId, 1L));
        verify(accessService, times(1)).checkUser(userId);
    }

    @Test
    void deleteDeviceTest_success() {
        when(deviceRepository.findByUserIdAndDeviceId(userId, 1L))
                .thenReturn(Optional.of(device));

        connectedDeviceService.deleteDevice(userId, 1L);

        verify(accessService, times(1)).checkUser(userId);
        verify(deviceRepository, times(1)).delete(device);
    }

    @Test
    void deleteDeviceTest_deviceNotFound() {
        when(deviceRepository.findByUserIdAndDeviceId(userId, 1L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> connectedDeviceService.deleteDevice(userId, 1L));
        verify(accessService, times(1)).checkUser(userId);
    }
}