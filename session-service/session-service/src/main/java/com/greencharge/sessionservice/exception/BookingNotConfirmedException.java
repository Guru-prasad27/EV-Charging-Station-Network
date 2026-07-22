package com.greencharge.sessionservice.exception;

public class BookingNotConfirmedException extends RuntimeException {

    public BookingNotConfirmedException(String message) {
        super(message);
    }
}
