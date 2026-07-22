package com.greencharge.sessionservice.exception;

public class BookingServiceUnavailableException extends RuntimeException {

    public BookingServiceUnavailableException(String message) {
        super(message);
    }
}
