package com.volta.api.exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends VoltaException {
    public ResourceNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
