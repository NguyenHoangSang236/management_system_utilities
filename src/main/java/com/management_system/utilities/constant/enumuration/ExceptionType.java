package com.management_system.utilities.constant.enumuration;

public enum ExceptionType {
    VALIDATOR("validation_exception"),
    ACCESS("access_exception"),
    FIREBASE("firebase_exception"),
    DATA_NOT_FOUND("data_not_found_exception"),
    DATA_DUPLICATED("data_duplicated_exception"),
    JWT_EXPIRED("jwt_expired_exception"),
    INVALID_FORMAT("invalid_format_exception"),
    INVALID_PARAMETER("invalid_parameter_exception"),
    INTERNAL("internal_exception"),
    FILE("file_exception"),
    AUTHENTICATION("unauthorized_exception");

    private final String value;

    ExceptionType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
