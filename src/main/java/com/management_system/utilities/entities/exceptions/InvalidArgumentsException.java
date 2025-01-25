package com.management_system.utilities.entities.exceptions;

public class InvalidArgumentsException extends Exception{
    String message;

    public InvalidArgumentsException(String message) {
        super(message);
    }
}
