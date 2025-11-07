package org.wa.user.service.dto.user;

import lombok.Data;
import org.wa.user.service.model.enumeration.StatusEnum;

@Data
public class UserShortInfoDto {
    private Long id;
    private String email;
    private String phone;
    private StatusEnum status;
}
