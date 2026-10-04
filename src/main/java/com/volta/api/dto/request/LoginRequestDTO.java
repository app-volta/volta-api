package com.volta.api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(

        @NotBlank(message = "O e-mail é obrigatório")
        @Email
        String email,

        @NotBlank(message = "A senha é obrigatória")
        String password
) {
}
