package org.wa.user.service.dto.device;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.wa.user.service.model.enumeration.ConnectedDeviceTypeEnum;

@Data
public class ConnectedDeviceCreateDto {
    @NotNull(message = "Device type is required")
    private ConnectedDeviceTypeEnum deviceType;

    @NotBlank(message = "Device ID is required")
    private String deviceId;

    @NotBlank(message = "Device model is required")
    private String deviceModel;
}
