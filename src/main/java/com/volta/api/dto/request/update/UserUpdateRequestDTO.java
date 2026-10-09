package com.volta.api.dto.request.update;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados para atualização do próprio perfil")
public record UserUpdateRequestDTO(

        @Schema(description = "Nome completo", example = "Maria Silva")
        @NotBlank(message = "O nome é obrigatório")
        @Size(max = 150)
        String name,

        @Schema(description = "Cargo do usuário", example = "Coordenadora ambiental")
        @Size(max = 100)
        String position
) {
}
