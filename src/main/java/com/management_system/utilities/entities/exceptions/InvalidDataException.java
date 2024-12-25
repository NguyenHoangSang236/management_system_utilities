package com.management_system.utilities.entities.exceptions;

public class InvalidDataException extends RuntimeException {
    String message;

    public InvalidDataException(String message) {
        super(message);
    }
}
