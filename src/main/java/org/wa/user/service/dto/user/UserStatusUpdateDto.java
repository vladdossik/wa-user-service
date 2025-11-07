package org.wa.user.service.dto.user;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.wa.user.service.model.enumeration.StatusEnum;

@Data
public class UserStatusUpdateDto {
    @NotNull(message = "Status is required")
    private StatusEnum status;
}
