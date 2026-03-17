package org.wa.user.service.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisteredDto {
    private UUID externalId;
    private String phone;
    private String email;
}
