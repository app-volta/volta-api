package com.volta.api.handler;

import com.volta.api.exception.BusinessRuleException;
import com.volta.api.exception.ConflictException;
import com.volta.api.exception.ResourceNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.sql.SQLException;
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
        bindingResult.addError(new FieldError("request", "email", null, false, null, null, "Email is required"));
        bindingResult.addError(new FieldError("request", "email", null, false, null, null, "Invalid email"));
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
                "email", "Email is required",
                "password", "Invalid Value"));
    }

    @Test
    @DisplayName("RAISE EXCEPTION de procedure devolve 422 sem expor a mensagem do banco")
    void handleDataAccess_procedureRaiseException_returns422() {
        SQLException sqlException = new SQLException("Collection with id x not found.", "P0001");
        ProblemDetail response = handler.handleDataAccess(
                new UncategorizedSQLException("call", "CALL update_collection_status(?, ?, ?)", sqlException));

        assertThat(response.getStatus()).isEqualTo(422);
        assertThat(response.getDetail()).isEqualTo("Operation rejected by a database business rule");
    }

    @Test
    @DisplayName("outro erro de acesso a dados devolve 500 genérico")
    void handleDataAccess_otherDatabaseError_returns500() {
        ProblemDetail response = handler.handleDataAccess(
                new DataAccessResourceFailureException("connection refused"));

        assertThat(response.getStatus()).isEqualTo(500);
        assertThat(response.getDetail()).isEqualTo("Internal Error");
    }

    @Test
    @DisplayName("falha interna na autenticação devolve 500 e não 401")
    void handleInternalAuthentication_internalFailure_returns500() {
        ProblemDetail response = handler.handleInternalAuthentication(
                new InternalAuthenticationServiceException("database down"));

        assertThat(response.getStatus()).isEqualTo(500);
    }
}
