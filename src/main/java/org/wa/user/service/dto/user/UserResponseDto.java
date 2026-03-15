package org.wa.user.service.dto.user;

import lombok.Data;
import org.wa.user.service.entity.enumeration.Gender;
import org.wa.user.service.entity.enumeration.Status;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
public class UserResponseDto {
    private UUID externalId;
    private String email;
    private String phone;
    private LocalDateTime birthday;
    private Gender gender;
    private Integer height;
    private Integer weight;
    private Status status;
    private OffsetDateTime createdAt;
    private OffsetDateTime modifiedAt;
}
