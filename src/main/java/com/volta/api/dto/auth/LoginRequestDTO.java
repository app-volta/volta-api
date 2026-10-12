package com.volta.api.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Credenciais de login")
public record LoginRequestDTO(

        @Schema(description = "E-mail do usuário", example = "admin@volta.com")
        @NotBlank(message = "Email is required")
        @Email
        String email,

        @Schema(description = "Senha do usuário", example = "senha12345")
        @NotBlank(message = "Password is required")
        String password
) {
}
