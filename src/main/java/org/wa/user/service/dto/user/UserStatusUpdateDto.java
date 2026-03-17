package org.wa.user.service.dto.user;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.wa.user.service.entity.enumeration.Status;

@Data
public class UserStatusUpdateDto {
    @NotNull(message = "Требуется значение статуса (PENDING, ACTIVE, BLOCKED, DELETED)")
    private Status status;
}
