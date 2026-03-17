package org.wa.user.service.dto.device;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ConnectedDeviceUpdateDto {
    @NotBlank(message = "Требуется модель устройства")
    private String deviceModel;
    private Boolean isActive;
}