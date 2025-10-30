package org.wa.user.service.exception;

public class DuplicatePhoneException extends RuntimeException {
    public DuplicatePhoneException(String message) {
        super(message);
    }
}
