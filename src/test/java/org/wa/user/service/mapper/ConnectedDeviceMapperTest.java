package org.wa.user.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.wa.user.service.dto.device.ConnectedDeviceCreateDto;
import org.wa.user.service.dto.device.ConnectedDeviceResponseDto;
import org.wa.user.service.dto.device.ConnectedDeviceUpdateDto;
import org.wa.user.service.entity.ConnectedDeviceEntity;
import org.wa.user.service.entity.UserEntity;
import org.wa.user.service.util.Initializer;
import java.time.OffsetDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mapstruct.factory.Mappers.getMapper;

public class ConnectedDeviceMapperTest {
    private final ConnectedDeviceMapper connectedDeviceMapper = getMapper(ConnectedDeviceMapper.class);

    private ConnectedDeviceEntity device;
    private ConnectedDeviceCreateDto createDto;
    private ConnectedDeviceUpdateDto updateDto;
    private UserEntity user;

    @BeforeEach
    void setUp() {
        user = Initializer.createTestUser();
        device = Initializer.createTestDevice(user);
        createDto = Initializer.createTestDeviceCreateDto();
        updateDto = Initializer.createTestDeviceUpdateDto();
    }

    @Test
    void toResponseDtoTest() {
        ConnectedDeviceResponseDto responseDto = connectedDeviceMapper.toResponseDto(device);

        assertNotNull(responseDto);
        assertEquals(device.getId(), responseDto.getId());
        assertEquals(device.getDeviceType(), responseDto.getDeviceType());
        assertEquals(device.getDeviceId(), responseDto.getDeviceId());
        assertEquals(device.getDeviceModel(), responseDto.getDeviceModel());
        assertEquals(device.getIsActive(), responseDto.getIsActive());
        assertEquals(device.getLastSyncAt(), responseDto.getLastSyncAt());
    }

    @Test
    void toResponseDtoNullTest() {
        assertNull(connectedDeviceMapper.toResponseDto(null));
    }

    @Test
    void toResponseDtoListTest() {
        List<ConnectedDeviceEntity> devices = List.of(device);
        List<ConnectedDeviceResponseDto> responseDto = connectedDeviceMapper.toResponseDtoList(devices);

        assertNotNull(responseDto);
        assertEquals(1, responseDto.size());
        assertEquals(device.getId(), responseDto.getFirst().getId());
        assertEquals(device.getDeviceType(), responseDto.getFirst().getDeviceType());
    }

    @Test
    void toResponseDtoListEmptyTest() {
        List<ConnectedDeviceResponseDto> responseDto = connectedDeviceMapper.toResponseDtoList(List.of());

        assertNotNull(responseDto);
        assertTrue(responseDto.isEmpty());
    }

    @Test
    void toResponseDtoListNullTest() {
        assertNull(connectedDeviceMapper.toResponseDtoList(null));
    }

    @Test
    void toEntityTest() {
        ConnectedDeviceEntity entity = connectedDeviceMapper.toEntity(createDto);

        assertNotNull(entity);
        assertNull(entity.getId());
        assertNull(entity.getUser());
        assertNull(entity.getIsActive());
        assertNull(entity.getLastSyncAt());
        assertEquals(createDto.getDeviceType(), entity.getDeviceType());
        assertEquals(createDto.getDeviceId(), entity.getDeviceId());
        assertEquals(createDto.getDeviceModel(), entity.getDeviceModel());
    }

    @Test
    void toEntityNullTest() {
        assertNull(connectedDeviceMapper.toEntity(null));
    }

    @Test
    void updateEntityFromDtoTest() {
        ConnectedDeviceEntity existingDevice = Initializer.createTestDevice(user);
        existingDevice.setDeviceModel("OldModel");
        existingDevice.setIsActive(false);

        connectedDeviceMapper.updateEntityFromDto(updateDto, existingDevice);

        assertEquals(updateDto.getDeviceModel(), existingDevice.getDeviceModel());
        assertEquals(updateDto.getIsActive(), existingDevice.getIsActive());
        assertNotNull(existingDevice.getId());
        assertNotNull(existingDevice.getUser());
        assertNotNull(existingDevice.getDeviceType());
        assertNotNull(existingDevice.getDeviceId());
        assertNotNull(existingDevice.getLastSyncAt());
    }

    @Test
    void updateEntityFromDtoPartialTest() {
        ConnectedDeviceEntity existingDevice = Initializer.createTestDevice(user);
        Boolean originalIsActive = existingDevice.getIsActive();

        ConnectedDeviceUpdateDto partialUpdateDto = new ConnectedDeviceUpdateDto();
        partialUpdateDto.setDeviceModel("UpdatedModel");

        connectedDeviceMapper.updateEntityFromDto(partialUpdateDto, existingDevice);

        assertEquals("UpdatedModel", existingDevice.getDeviceModel());
        assertEquals(originalIsActive, existingDevice.getIsActive());
    }

    @Test
    void updateEntityFromDtoNullTest() {
        ConnectedDeviceEntity existingDevice = Initializer.createTestDevice(user);
        String originalDeviceModel = existingDevice.getDeviceModel();
        Boolean originalIsActive = existingDevice.getIsActive();

        connectedDeviceMapper.updateEntityFromDto(null, existingDevice);

        assertEquals(originalDeviceModel, existingDevice.getDeviceModel());
        assertEquals(originalIsActive, existingDevice.getIsActive());
    }

    @Test
    void setDeviceDefaultsTest() {
        ConnectedDeviceEntity deviceEntity = connectedDeviceMapper.setDeviceDefaults(createDto, user);

        assertNotNull(deviceEntity);
        assertEquals(user, deviceEntity.getUser());
        assertTrue(deviceEntity.getIsActive());
        assertNotNull(deviceEntity.getLastSyncAt());
        assertTrue(deviceEntity.getLastSyncAt().isBefore(OffsetDateTime.now().plusSeconds(1)));
        assertTrue(deviceEntity.getLastSyncAt().isAfter(OffsetDateTime.now().minusSeconds(1)));
        assertEquals(createDto.getDeviceType(), deviceEntity.getDeviceType());
        assertEquals(createDto.getDeviceId(), deviceEntity.getDeviceId());
        assertEquals(createDto.getDeviceModel(), deviceEntity.getDeviceModel());
    }
}
