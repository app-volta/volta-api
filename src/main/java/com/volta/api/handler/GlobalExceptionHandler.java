package com.volta.api.handler;

import com.volta.api.exception.VoltaException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.security.access.AccessDeniedException;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(VoltaException.class)
    public ProblemDetail handleVolta(VoltaException exception) {
        HttpStatus status = exception.getStatus();
        String message = exception.getMessage();
        return ProblemDetail.forStatusAndDetail(status, message);
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneric(Exception exception) {
        exception.printStackTrace();
        return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Error");
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail handleDataIntegrity(DataIntegrityViolationException exception) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, "CNPJ already registered");
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ProblemDetail handleAccessDenied(AccessDeniedException exception) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, "You do not have permission to access this resource");
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : exception.getBindingResult().getFieldErrors()) {

            String fieldName = error.getField();
            String message;

            if (error.getDefaultMessage() != null) {
                message = error.getDefaultMessage();
            } else {
                message = "Invalid Value";
            }

            if (!errors.containsKey(fieldName)) {
                errors.put(fieldName, message);
            }
        }

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Validation error");
        problem.setProperty("errors", errors);
        return ResponseEntity.badRequest().body(problem);
    }
}
