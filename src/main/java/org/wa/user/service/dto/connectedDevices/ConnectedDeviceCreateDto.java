package org.wa.user.service.dto.connectedDevices;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.wa.user.service.model.enums.ConnectedDevicesTypeEnum;

@Data
public class ConnectedDeviceCreateDto {
    @NotBlank(message = "Device type is required")
    private ConnectedDevicesTypeEnum deviceType;

    @NotBlank(message = "Device ID is required")
    private String deviceId;

    @NotBlank(message = "Device model is required")
    private String deviceModel;
}
