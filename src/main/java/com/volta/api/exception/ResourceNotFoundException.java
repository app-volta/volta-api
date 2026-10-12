package com.volta.api.exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends VoltaException {
    public ResourceNotFoundException(String resource) {
        super(resource + " not found", HttpStatus.NOT_FOUND);
    }
}