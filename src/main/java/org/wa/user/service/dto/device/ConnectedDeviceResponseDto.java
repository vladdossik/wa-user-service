package org.wa.user.service.dto.device;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.wa.user.service.entity.enumeration.ConnectedDeviceType;
import java.time.OffsetDateTime;

@Data
public class ConnectedDeviceResponseDto {
    private Long id;
    private ConnectedDeviceType deviceType;
    private String deviceId;
    private String deviceModel;
    private Boolean isActive;
    @JsonFormat(pattern = "dd.MM.yyyy HH:mm:ss Z")
    private OffsetDateTime lastSyncAt;
}
