package com.volta.api.exception.handler;

import com.volta.api.exception.VoltaException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(VoltaException.class)
    public ProblemDetail handleVolta(VoltaException exception) {
        HttpStatus status = exception.getStatus();
        String message = exception.getMessage();
        return ProblemDetail.forStatusAndDetail(status, message);
    }
}
