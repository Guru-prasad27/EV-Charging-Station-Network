package com.skillfirstlab.chargingstationservice_1.exception;

public class StationNotFoundException extends RuntimeException {

    public StationNotFoundException(String message) {
        super(message);
    }

    public StationNotFoundException(Long id) {
        super("Charging station not found with id: " + id);
    }
}
