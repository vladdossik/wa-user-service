package org.wa.user.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
public class ErrorResponse {
    private String message;
    private int status;
    private OffsetDateTime timestamp;
}
