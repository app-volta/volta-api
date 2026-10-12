package com.volta.api.exception;

import org.springframework.http.HttpStatus;

public class ConflictException extends VoltaException {
    public ConflictException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
