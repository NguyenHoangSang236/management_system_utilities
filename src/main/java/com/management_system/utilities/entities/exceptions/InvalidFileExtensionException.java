package com.management_system.utilities.entities.exceptions;

public class InvalidFileExtensionException extends RuntimeException{
    String message;

    public InvalidFileExtensionException(String message) {
        super(message);
    }
}
