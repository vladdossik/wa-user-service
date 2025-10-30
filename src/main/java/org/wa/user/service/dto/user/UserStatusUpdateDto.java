package org.wa.user.service.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.wa.user.service.model.enums.StatusEnum;

@Data
public class UserStatusUpdateDto {
    @NotBlank(message = "Status is required")
    private StatusEnum status;
}
