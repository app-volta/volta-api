package com.volta.api.dto.scoring;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Tipo de resíduo usado no cálculo de score")
public record WasteTypeScoringDTO(
        @Schema(description = "Categoria normalizada (WasteCategory)", example = "PERIGOSO")
        String category,
        @Schema(description = "Risco padrão do tipo de resíduo", example = "HIGH")
        String defaultRiskLevel
) {
}
