package com.volta.api.exception;

import org.springframework.http.HttpStatus;

public class BusinessRuleException extends VoltaException {
    public BusinessRuleException(String message){
        super(message, HttpStatus.UNPROCESSABLE_CONTENT);
    }
}
