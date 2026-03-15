package org.wa.user.service.dto.device;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.wa.user.service.entity.enumeration.ConnectedDeviceType;

@Data
public class ConnectedDeviceCreateDto {
    @NotNull(message = "Требуется тип устройства")
    private ConnectedDeviceType deviceType;
    @NotBlank(message = "Требуется ID устройства")
    private String deviceId;
    @NotBlank(message = "Требуется модель устройства")
    private String deviceModel;
}
