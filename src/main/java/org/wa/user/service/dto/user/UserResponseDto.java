package org.wa.user.service.dto.user;

import lombok.Data;
import org.wa.user.service.model.enumeration.GenderEnum;
import org.wa.user.service.model.enumeration.StatusEnum;
import java.time.OffsetDateTime;

@Data
public class UserResponseDto {
    private Long id;
    private String email;
    private String phone;
    private OffsetDateTime birthday;
    private GenderEnum gender;
    private Integer height;
    private Integer weight;
    private StatusEnum status;
    private OffsetDateTime createdAt;
    private OffsetDateTime modifiedAt;
}
