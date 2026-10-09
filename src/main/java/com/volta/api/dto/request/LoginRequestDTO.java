package com.volta.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Credenciais de login")
public record LoginRequestDTO(

        @Schema(description = "E-mail do usuário", example = "admin@volta.com")
        @NotBlank(message = "O e-mail é obrigatório")
        @Email
        String email,

        @Schema(description = "Senha do usuário", example = "senha12345")
        @NotBlank(message = "A senha é obrigatória")
        String password
) {
}
