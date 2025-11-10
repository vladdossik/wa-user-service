package org.wa.user.service.dto.device;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ConnectedDeviceUpdateDto {
    @NotBlank(message = "Device model is required")
    private String deviceModel;
    private Boolean isActive;
}