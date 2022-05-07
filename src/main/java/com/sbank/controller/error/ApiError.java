package com.sbank.controller.error;


public final class ApiError {

    private final String field, message;

    public String getField() {
        return field;
    }

    public String getMessage() {
        return message;
    }

    public ApiError(String field, String message) {
        this.field = field;
        this.message = message;
    }

}