package org.wa.user.service.dto.user;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.wa.user.service.entity.enumeration.Status;

@Data
public class UserStatusUpdateDto {
    @NotNull(message = "Status is required")
    private Status status;
}
