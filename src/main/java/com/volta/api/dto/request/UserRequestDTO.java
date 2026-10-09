package com.volta.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.UUID;

@Schema(description = "Dados para cadastro de usuário")
@Builder
public record UserRequestDTO(

        @Schema(description = "Nome completo", example = "Maria Silva")
        @NotBlank(message = "O nome é obrigatório")
        @Size(max = 150)
        String name,

        @Schema(description = "E-mail usado no login", example = "maria.silva@volta.com")
        @NotBlank(message = "O e-mail é obrigatório")
        @Email
        @Size(max = 150)
        String email,

        @Schema(description = "Senha (8 a 72 caracteres)", example = "senha12345")
        @NotBlank(message = "A senha é obrigatória")
        @Size(min = 8, max = 72)
        String password,

        @Schema(description = "Cargo do usuário", example = "Analista ambiental")
        @Size(max = 100)
        String position,

        @Schema(description = "ID da empresa do usuário", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
        @NotNull
        UUID companyId
) {
}
