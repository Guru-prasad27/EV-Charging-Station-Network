package com.greencharge.paymentservice.exception;

public class InvalidRefundRequestException extends RuntimeException {

    public InvalidRefundRequestException(String message) {
        super(message);
    }
}
