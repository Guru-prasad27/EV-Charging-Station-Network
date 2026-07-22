package com.skillfirstlab.chargingstationservice_1.exception;

public class ConnectorNotSupportedException extends RuntimeException {

    public ConnectorNotSupportedException(String message) {
        super(message);
    }
}
