package org.wa.user.service.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.wa.user.service.mapper.ErrorMapper;
import org.wa.user.service.model.ErrorResponse;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final ErrorMapper errorMapper;

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateEmail(DuplicateEmailException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(DuplicatePhoneException.class)
    public ResponseEntity<ErrorResponse> handleDuplicatePhone(DuplicatePhoneException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(DuplicateDeviceException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateDevice(DuplicateDeviceException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UserProfileAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserProfileAlreadyExists(UserProfileAlreadyExistsException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.CONFLICT);
    }

    private ResponseEntity<ErrorResponse> buildResponse(String message, HttpStatus status) {
        return ResponseEntity
                .status(status)
                .body(errorMapper.toErrorResponse(message, status));
    }
}
