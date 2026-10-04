package com.volta.api.exception;

import org.springframework.http.HttpStatus;

public class ForbiddenException extends VoltaException {
    public ForbiddenException(String message) {
        super(message, HttpStatus.FORBIDDEN);
    }
}
