package com.greencharge.sessionservice.exception;

public class SessionAlreadyEndedException extends RuntimeException {

    public SessionAlreadyEndedException(String message) {
        super(message);
    }
}
