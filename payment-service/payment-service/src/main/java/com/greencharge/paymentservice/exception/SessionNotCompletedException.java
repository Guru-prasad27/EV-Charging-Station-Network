package com.greencharge.paymentservice.exception;

public class SessionNotCompletedException extends RuntimeException {

    public SessionNotCompletedException(String message) {
        super(message);
    }
}
