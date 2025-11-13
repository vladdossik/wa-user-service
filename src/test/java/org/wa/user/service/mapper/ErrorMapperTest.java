package org.wa.user.service.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.wa.user.service.entity.ErrorResponse;
import java.time.OffsetDateTime;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mapstruct.factory.Mappers.getMapper;

public class ErrorMapperTest {
    private final ErrorMapper errorMapper = getMapper(ErrorMapper.class);

    @Test
    void toErrorResponseTest() {
        String message = "Test error message";
        HttpStatus status = HttpStatus.BAD_REQUEST;

        ErrorResponse errorResponse = errorMapper.toErrorResponse(message, status);

        assertNotNull(errorResponse);
        assertEquals(message, errorResponse.getMessage());
        assertEquals(status.value(), errorResponse.getStatus());
        assertNotNull(errorResponse.getTimestamp());
        assertTrue(errorResponse.getTimestamp().isBefore(OffsetDateTime.now().plusSeconds(1)));
        assertTrue(errorResponse.getTimestamp().isAfter(OffsetDateTime.now().minusSeconds(1)));
    }

    @Test
    void toErrorResponseDifferentStatusTest() {
        String message = "Not found";
        HttpStatus status = HttpStatus.NOT_FOUND;

        ErrorResponse errorResponse = errorMapper.toErrorResponse(message, status);

        assertNotNull(errorResponse);
        assertEquals(message, errorResponse.getMessage());
        assertEquals(status.value(), errorResponse.getStatus());
        assertNotNull(errorResponse.getTimestamp());
    }
}
