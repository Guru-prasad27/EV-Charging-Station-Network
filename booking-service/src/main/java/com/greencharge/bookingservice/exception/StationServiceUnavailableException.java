package com.greencharge.bookingservice.exception;

public class StationServiceUnavailableException extends RuntimeException {

    public StationServiceUnavailableException(String message) {
        super(message);
    }
}
