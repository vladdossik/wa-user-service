package org.wa.user.service.exception;

public class DuplicateDeviceException extends RuntimeException {
    public DuplicateDeviceException(String message) {
        super(message);
    }
}
