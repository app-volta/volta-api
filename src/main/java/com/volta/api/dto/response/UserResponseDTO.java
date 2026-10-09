package com.volta.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

@Schema(description = "Usuário")
public record UserResponseDTO(
        @Schema(description = "ID do usuário", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
        UUID id,
        @Schema(description = "Nome completo", example = "Maria Silva")
        String name,
        @Schema(description = "E-mail", example = "maria.silva@volta.com")
        String email,
        @Schema(description = "Cargo", example = "Analista ambiental")
        String position,
        @Schema(description = "ID da empresa", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
        UUID companyId,
        @Schema(description = "Perfil de acesso", example = "MANAGER", allowableValues = {"ADMIN", "MANAGER", "EMPLOYEE", "OPERATOR"})
        String role

) {
}
