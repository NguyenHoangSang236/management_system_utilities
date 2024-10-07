package com.management_system.utilities.entities.exceptions;

public class InsertValidationException extends RuntimeException {
    String message;

    public InsertValidationException(String message) {
        super(message);
    }
}
