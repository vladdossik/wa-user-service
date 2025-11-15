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
    private UserService userService;

    @Mock
    private ConnectedDeviceMapper deviceMapper;

    @InjectMocks
    private ConnectedDeviceServiceImpl connectedDeviceService;

    private UserEntity user;
    private ConnectedDeviceEntity device;
    private ConnectedDeviceCreateDto deviceCreateDto;
    private ConnectedDeviceUpdateDto deviceUpdateDto;
    private ConnectedDeviceResponseDto deviceResponseDto;

    @BeforeEach
    void setUp() {
        user = Initializer.createTestUser();
        device = Initializer.createTestDevice(user);
        deviceCreateDto = Initializer.createTestDeviceCreateDto();
        deviceUpdateDto = Initializer.createTestDeviceUpdateDto();
        deviceResponseDto = Initializer.createTestDeviceResponseDto();
    }

    @Test
    void getUserDevicesTest_success() {
        when(userService.userExists(1L)).thenReturn(true);
        when(deviceRepository.findByUserId(1L)).thenReturn(List.of(device));
        when(deviceMapper.toResponseDtoList(List.of(device))).thenReturn(List.of(deviceResponseDto));

        List<ConnectedDeviceResponseDto> response = connectedDeviceService.getUserDevices(1L);

        assertNotNull(response);
        assertEquals(List.of(deviceResponseDto), response);
        verify(deviceRepository, times(1)).findByUserId(1L);
    }

    @Test
    void getUserDevicesTest_userNotFound() {
        when(userService.userExists(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> connectedDeviceService.getUserDevices(1L));
    }

    @Test
    void addUserDeviceTest_success() {
        when(userService.getUserEntity(1L)).thenReturn(user);
        when(deviceRepository.existsByDeviceId("device-123")).thenReturn(false);
        when(deviceMapper.setDeviceDefaults(deviceCreateDto, user)).thenReturn(device);
        when(deviceRepository.save(device)).thenReturn(device);
        when(deviceMapper.toResponseDto(device)).thenReturn(deviceResponseDto);

        ConnectedDeviceResponseDto response = connectedDeviceService.addUserDevice(1L, deviceCreateDto);

        assertNotNull(response);
        verify(deviceMapper, times(1)).setDeviceDefaults(deviceCreateDto, user);
        verify(deviceRepository, times(1)).save(device);
        verify(deviceMapper, times(1)).toResponseDto(device);
    }

    @Test
    void addUserDeviceTest_deviceExists() {
        when(userService.getUserEntity(1L)).thenReturn(user);
        when(deviceRepository.existsByDeviceId("device-123")).thenReturn(true);

        assertThrows(AttributeDuplicateException.class,
                () -> connectedDeviceService.addUserDevice(1L, deviceCreateDto));
    }

    @Test
    void addUserDeviceTest_userNotFound() {
        when(userService.getUserEntity(1L))
                .thenThrow(new ResourceNotFoundException("User not found"));

        assertThrows(ResourceNotFoundException.class,
                () -> connectedDeviceService.addUserDevice(1L, deviceCreateDto));
    }

    @Test
    void updateUserDeviceTest_success() {
        when(userService.userExists(1L)).thenReturn(true);
        when(deviceRepository.findByUserIdAndDeviceId(1L, 1L))
                .thenReturn(Optional.of(device));
        when(deviceRepository.save(device)).thenReturn(device);
        when(deviceMapper.toResponseDto(device)).thenReturn(deviceResponseDto);

        ConnectedDeviceResponseDto response = connectedDeviceService.updateUserDevice(
                1L, 1L, deviceUpdateDto);

        assertNotNull(response);
        verify(deviceMapper, times(1)).updateEntityFromDto(deviceUpdateDto, device);
        verify(deviceRepository, times(1)).save(device);
    }

    @Test
    void updateUserDeviceTest_userNotFound() {
        when(userService.userExists(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                () -> connectedDeviceService.updateUserDevice(1L, 1L, deviceUpdateDto));
    }

    @Test
    void updateUserDeviceTest_deviceNotFound() {
        when(userService.userExists(1L)).thenReturn(true);
        when(deviceRepository.findByUserIdAndDeviceId(1L, 1L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> connectedDeviceService.updateUserDevice(1L, 1L, deviceUpdateDto));
    }

    @Test
    void syncDeviceTest_success() {
        when(deviceRepository.findByUserIdAndDeviceId(1L, 1L))
                .thenReturn(Optional.of(device));
        when(deviceRepository.save(device)).thenReturn(device);
        when(deviceMapper.toResponseDto(device)).thenReturn(deviceResponseDto);

        ConnectedDeviceResponseDto response = connectedDeviceService.syncDevice(1L, 1L);

        assertNotNull(response);
        assertNotNull(device.getLastSyncAt());
        verify(deviceRepository, times(1)).save(device);
    }

    @Test
    void syncDeviceTest_deviceNotFound() {
        when(deviceRepository.findByUserIdAndDeviceId(1L, 1L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> connectedDeviceService.syncDevice(1L, 1L));
    }

    @Test
    void deleteDeviceTest_success() {
        when(deviceRepository.findByUserIdAndDeviceId(1L, 1L))
                .thenReturn(Optional.of(device));

        connectedDeviceService.deleteDevice(1L, 1L);

        verify(deviceRepository, times(1)).delete(device);
    }

    @Test
    void deleteDeviceTest_deviceNotFound() {
        when(deviceRepository.findByUserIdAndDeviceId(1L, 1L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> connectedDeviceService.deleteDevice(1L, 1L));
    }
}