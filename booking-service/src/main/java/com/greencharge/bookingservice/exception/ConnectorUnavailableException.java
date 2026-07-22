package com.greencharge.bookingservice.exception;

public class ConnectorUnavailableException extends RuntimeException {

    public ConnectorUnavailableException(String message) {
        super(message);
    }
}
