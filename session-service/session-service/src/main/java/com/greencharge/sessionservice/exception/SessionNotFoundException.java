package com.greencharge.sessionservice.exception;

public class SessionNotFoundException extends RuntimeException {

    public SessionNotFoundException(String message) {
        super(message);
    }

    public SessionNotFoundException(Long id) {
        super("Charging session not found with id: " + id);
    }
}
