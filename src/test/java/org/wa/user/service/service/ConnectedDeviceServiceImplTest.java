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
    void getUserDevicesSuccessTest() {
        when(userService.userExists(Initializer.TEST_ID)).thenReturn(true);
        when(deviceRepository.findByUserId(Initializer.TEST_ID)).thenReturn(List.of(device));
        when(deviceMapper.toResponseDtoList(List.of(device))).thenReturn(List.of(deviceResponseDto));

        List<ConnectedDeviceResponseDto> response = connectedDeviceService.getUserDevices(Initializer.TEST_ID);

        assertNotNull(response);
        assertEquals(List.of(deviceResponseDto), response);
        verify(deviceRepository).findByUserId(Initializer.TEST_ID);
    }

    @Test
    void getUserDevicesWhenUserNotFoundTest() {
        when(userService.userExists(Initializer.TEST_ID)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                () -> connectedDeviceService.getUserDevices(Initializer.TEST_ID));
    }

    @Test
    void addUserDeviceSuccessTest() {
        when(userService.getUserEntity(Initializer.TEST_ID)).thenReturn(user);
        when(deviceRepository.existsByDeviceId(Initializer.TEST_DEVICE_ID)).thenReturn(false);
        when(deviceMapper.setDeviceDefaults(deviceCreateDto, user)).thenReturn(device);
        when(deviceRepository.save(device)).thenReturn(device);
        when(deviceMapper.toResponseDto(device)).thenReturn(deviceResponseDto);

        ConnectedDeviceResponseDto response = connectedDeviceService.addUserDevice(
                Initializer.TEST_ID, deviceCreateDto);

        assertNotNull(response);
        verify(deviceMapper).setDeviceDefaults(deviceCreateDto, user);
        verify(deviceRepository).save(device);
        verify(deviceMapper).toResponseDto(device);
    }

    @Test
    void addUserDeviceWhenDeviceExistsTest() {
        when(userService.getUserEntity(Initializer.TEST_ID)).thenReturn(user);
        when(deviceRepository.existsByDeviceId(Initializer.TEST_DEVICE_ID)).thenReturn(true);

        assertThrows(AttributeDuplicateException.class,
                () -> connectedDeviceService.addUserDevice(Initializer.TEST_ID, deviceCreateDto));
    }

    @Test
    void addUserDeviceWhenUserNotFoundTest() {
        when(userService.getUserEntity(Initializer.TEST_ID))
                .thenThrow(new ResourceNotFoundException("User not found"));

        assertThrows(ResourceNotFoundException.class,
                () -> connectedDeviceService.addUserDevice(Initializer.TEST_ID, deviceCreateDto));
    }

    @Test
    void updateUserDeviceSuccessTest() {
        when(userService.userExists(Initializer.TEST_ID)).thenReturn(true);
        when(deviceRepository.findByUserIdAndDeviceId(Initializer.TEST_ID, Initializer.TEST_ID))
                .thenReturn(Optional.of(device));
        when(deviceRepository.save(device)).thenReturn(device);
        when(deviceMapper.toResponseDto(device)).thenReturn(deviceResponseDto);

        ConnectedDeviceResponseDto response = connectedDeviceService.updateUserDevice(
                Initializer.TEST_ID, Initializer.TEST_ID, deviceUpdateDto);

        assertNotNull(response);
        verify(deviceMapper).updateEntityFromDto(deviceUpdateDto, device);
        verify(deviceRepository).save(device);
    }

    @Test
    void updateUserDeviceWhenUserNotFoundTest() {
        when(userService.userExists(Initializer.TEST_ID)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                () -> connectedDeviceService.updateUserDevice(Initializer.TEST_ID,
                        Initializer.TEST_ID, deviceUpdateDto));
    }

    @Test
    void updateUserDeviceWhenDeviceNotFoundTest() {
        when(userService.userExists(Initializer.TEST_ID)).thenReturn(true);
        when(deviceRepository.findByUserIdAndDeviceId(Initializer.TEST_ID, Initializer.TEST_ID))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> connectedDeviceService.updateUserDevice(Initializer.TEST_ID,
                        Initializer.TEST_ID, deviceUpdateDto));
    }

    @Test
    void syncDeviceSuccessTest() {
        when(deviceRepository.findByUserIdAndDeviceId(Initializer.TEST_ID, Initializer.TEST_ID))
                .thenReturn(Optional.of(device));
        when(deviceRepository.save(device)).thenReturn(device);
        when(deviceMapper.toResponseDto(device)).thenReturn(deviceResponseDto);

        ConnectedDeviceResponseDto response = connectedDeviceService.syncDevice(
                Initializer.TEST_ID, Initializer.TEST_ID);

        assertNotNull(response);
        assertNotNull(device.getLastSyncAt());
        verify(deviceRepository).save(device);
    }

    @Test
    void syncDeviceWhenDeviceNotFoundTest() {
        when(deviceRepository.findByUserIdAndDeviceId(Initializer.TEST_ID, Initializer.TEST_ID))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> connectedDeviceService.syncDevice(Initializer.TEST_ID, Initializer.TEST_ID));
    }

    @Test
    void deleteDeviceSuccessTest() {
        when(deviceRepository.findByUserIdAndDeviceId(Initializer.TEST_ID, Initializer.TEST_ID))
                .thenReturn(Optional.of(device));

        connectedDeviceService.deleteDevice(Initializer.TEST_ID, Initializer.TEST_ID);

        verify(deviceRepository).delete(device);
    }

    @Test
    void deleteDeviceWhenNotFoundTest() {
        when(deviceRepository.findByUserIdAndDeviceId(Initializer.TEST_ID, Initializer.TEST_ID))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> connectedDeviceService.deleteDevice(Initializer.TEST_ID, Initializer.TEST_ID));
    }
}