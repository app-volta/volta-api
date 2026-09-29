package com.volta.api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.UUID;

@Builder
public record UserRequestDTO(

        @NotBlank(message = "O nome é obrigatório")
        @Size(max = 150)
        String name,

        @NotBlank(message = "O e-mail é obrigatório")
        @Email
        @Size(max = 150)
        String email,

        @NotBlank(message = "A senha é obrigatória")
        @Size(min = 8, max = 72)
        String password,

        @Size(max = 150)
        String position,

        @NotNull
        UUID companyId
){}
