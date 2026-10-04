package com.volta.api.exception;

import org.springframework.http.HttpStatus;

public class ExternalServiceException extends VoltaException {
    public ExternalServiceException(String message) {
        super(message, HttpStatus.SERVICE_UNAVAILABLE);
    }
}
