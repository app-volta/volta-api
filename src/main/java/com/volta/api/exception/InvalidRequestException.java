package com.volta.api.exception;

import org.springframework.http.HttpStatus;

public class InvalidRequestException extends VoltaException {
    public InvalidRequestException(String message){
        super(message, HttpStatus.BAD_REQUEST);
    }
}
