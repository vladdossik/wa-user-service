package org.wa.user.service.dto.user;

import lombok.Data;
import org.wa.user.service.entity.enumeration.Status;
import java.util.UUID;

@Data
public class UserShortInfoDto {
    private UUID id;
    private String email;
    private String phone;
    private Status status;
}
