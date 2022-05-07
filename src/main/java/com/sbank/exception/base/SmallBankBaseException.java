package com.sbank.exception.base;

import org.springframework.http.HttpStatus;

public class SmallBankBaseException extends RuntimeException {

    public String getField() {
        return field;
    }

    private final String field;

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    private final HttpStatus httpStatus;

    public SmallBankBaseException(String field, String message, HttpStatus httpStatus) {
        super(message);
        this.field = field;
        this.httpStatus = httpStatus;
    }
}
