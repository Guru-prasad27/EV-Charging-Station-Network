package com.greencharge.paymentservice.exception;

public class SessionServiceUnavailableException extends RuntimeException {

    public SessionServiceUnavailableException(String message) {
        super(message);
    }
}
