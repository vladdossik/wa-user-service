package org.wa.user.service.exception;

public class UserProfileAlreadyExistsException extends RuntimeException {
    public UserProfileAlreadyExistsException(String message) {
        super(message);
    }
}
