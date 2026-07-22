package com.greencharge.sessionservice.exception;

public class SessionAlreadyActiveException extends RuntimeException {

    public SessionAlreadyActiveException(String message) {
        super(message);
    }
}
