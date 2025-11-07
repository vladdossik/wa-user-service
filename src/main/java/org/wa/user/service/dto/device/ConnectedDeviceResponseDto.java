package org.wa.user.service.dto.device;

import lombok.Data;
import org.wa.user.service.model.enumeration.ConnectedDeviceTypeEnum;
import java.time.OffsetDateTime;

@Data
public class ConnectedDeviceResponseDto {
    private Long id;
    private ConnectedDeviceTypeEnum deviceType;
    private String deviceId;
    private String deviceModel;
    private Boolean isActive;
    private OffsetDateTime lastSyncAt;
}
