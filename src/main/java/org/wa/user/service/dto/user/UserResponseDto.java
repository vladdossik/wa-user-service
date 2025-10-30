package org.wa.user.service.dto.user;

import lombok.Data;
import org.wa.user.service.model.enums.GenderEnum;
import org.wa.user.service.model.enums.StatusEnum;
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
    private OffsetDateTime modifyAt;
}
