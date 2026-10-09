package com.volta.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

@Schema(description = "Tipo de resíduo")
public record WasteTypeResponseDTO(

        @Schema(description = "ID do tipo de resíduo", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
        UUID id,
        @Schema(description = "Categoria", example = "Óleo lubrificante")
        String category,
        @Schema(description = "Descrição", example = "Óleo usado em máquinas industriais")
        String description,
        @Schema(description = "Nível de risco padrão", example = "HIGH", allowableValues = {"LOW", "MEDIUM", "HIGH"})
        String defaultRiskLevel
) {
}
