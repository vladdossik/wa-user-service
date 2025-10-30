package org.wa.user.service.dto.connectedDevices;

import lombok.Data;
import org.wa.user.service.model.enums.ConnectedDevicesTypeEnum;
import java.time.OffsetDateTime;

@Data
public class ConnectedDeviceResponseDto {
    private Long id;
    private ConnectedDevicesTypeEnum deviceType;
    private String deviceId;
    private String deviceModel;
    private Boolean isActive;
    private OffsetDateTime lastSyncAt;
}
