package org.wa.user.service.dto.user;

import lombok.Data;
import org.wa.user.service.entity.enumeration.Status;

@Data
public class UserShortInfoDto {
    private Long id;
    private String email;
    private String phone;
    private Status status;
}
