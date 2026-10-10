package com.volta.api.handler;

import com.volta.api.exception.BusinessRuleException;
import com.volta.api.exception.ConflictException;
import com.volta.api.exception.ResourceNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("GlobalExceptionHandler")
class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    @DisplayName("exceções do Volta usam o status e a mensagem da própria exceção")
    void handleVolta_voltaException_usesOwnStatusAndMessage() {
        ProblemDetail notFound = handler.handleVolta(new ResourceNotFoundException("Company"));
        ProblemDetail conflict = handler.handleVolta(new ConflictException("Email already registered"));
        ProblemDetail businessRule = handler.handleVolta(new BusinessRuleException("You cannot change your own role"));

        assertThat(notFound.getStatus()).isEqualTo(404);
        assertThat(notFound.getDetail()).isEqualTo("Company not found");
        assertThat(conflict.getStatus()).isEqualTo(409);
        assertThat(businessRule.getStatus()).isEqualTo(422);
    }

    @Test
    @DisplayName("violação de integridade devolve 409 sem expor detalhes do banco")
    void handleDataIntegrity_violation_returns409WithoutDatabaseDetails() {
        ProblemDetail response = handler.handleDataIntegrity(
                new DataIntegrityViolationException("duplicate key value violates unique constraint users_email_key"));

        assertThat(response.getStatus()).isEqualTo(409);
        assertThat(response.getDetail()).isEqualTo("Operation conflicts with existing data");
    }

    @Test
    @DisplayName("acesso negado devolve 403")
    void handleAccessDenied_accessDenied_returns403() {
        ProblemDetail response = handler.handleAccessDenied(new AccessDeniedException("denied"));

        assertThat(response.getStatus()).isEqualTo(403);
    }

    @Test
    @DisplayName("falha de autenticação devolve 401 com mensagem genérica")
    void handleAuthentication_authenticationFailure_returns401WithGenericMessage() {
        ProblemDetail response = handler.handleAuthentication(new BadCredentialsException("wrong password for user x"));

        assertThat(response.getStatus()).isEqualTo(401);
        assertThat(response.getDetail()).isEqualTo("Invalid credentials");
    }

    @Test
    @DisplayName("erro inesperado devolve 500 sem vazar a mensagem interna")
    void handleGeneric_unexpectedError_returns500WithoutLeakingInternalMessage() {
        ProblemDetail response = handler.handleGeneric(new RuntimeException("internal secret"));

        assertThat(response.getStatus()).isEqualTo(500);
        assertThat(response.getDetail()).isEqualTo("Internal Error");
    }

    @Test
    @DisplayName("validação inválida devolve 400 com o primeiro erro de cada campo")
    void handleMethodArgumentNotValid_fieldErrors_returns400WithFirstErrorPerField() throws NoSuchMethodException {
        BindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "request");
        bindingResult.addError(new FieldError("request", "email", null, false, null, null, "O e-mail é obrigatório"));
        bindingResult.addError(new FieldError("request", "email", null, false, null, null, "E-mail inválido"));
        bindingResult.addError(new FieldError("request", "password", null, false, null, null, null));
        MethodParameter parameter = new MethodParameter(Object.class.getMethod("toString"), -1);
        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(parameter, bindingResult);

        ResponseEntity<Object> response = handler.handleMethodArgumentNotValid(
                exception, new HttpHeaders(), HttpStatus.BAD_REQUEST, null);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        ProblemDetail problem = (ProblemDetail) response.getBody();
        assertThat(problem).isNotNull();
        assertThat(problem.getDetail()).isEqualTo("Validation error");
        assertThat(problem.getProperties()).containsEntry("errors", Map.of(
                "email", "O e-mail é obrigatório",
                "password", "Invalid Value"));
    }
}
