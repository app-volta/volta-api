package com.volta.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "Tipo de resíduo")
public record WasteTypeResponseDTO(

        @Schema(description = "ID do tipo de resíduo", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
        UUID id,
        @Schema(description = "Categoria da coleta seletiva (CONAMA 275/2001)", example = "PERIGOSO")
        String category,
        @Schema(description = "Descrição", example = "Óleo usado em máquinas industriais")
        String description,
        @Schema(description = "Nível de risco padrão", example = "HIGH", allowableValues = {"LOW", "MEDIUM", "HIGH"})
        String defaultRiskLevel,
        @Schema(description = "Cor do coletor (CONAMA 275/2001). Nulo se a categoria estiver fora do padrão", example = "Laranja")
        String color,
        @Schema(
                description = "Classificação NBR 10004: CLASSE_I (perigoso, risco HIGH) ou CLASSE_II (não perigoso)",
                example = "CLASSE_I",
                allowableValues = {"CLASSE_I", "CLASSE_II"}
        )
        String wasteClass
) {
}
