package org.wa.user.service.dto.connectedDevices;

import lombok.Data;

@Data
public class ConnectedDeviceUpdateDto {
    private String deviceModel;
    private Boolean isActive;
}
