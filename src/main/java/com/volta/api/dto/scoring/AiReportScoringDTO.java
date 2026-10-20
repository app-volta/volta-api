package com.volta.api.dto.scoring;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resumo do relatório de IA usado no cálculo de score")
public record AiReportScoringDTO(
        @Schema(description = "Tipo de resíduo identificado pela IA", example = "Óleo lubrificante")
        String detectedWasteType,
        @Schema(description = "Nível de contaminação estimado pela IA", example = "HIGH")
        String aiContaminationLevel,
        @Schema(description = "Recomendações de tratamento", example = "Isolar a área e acionar coleta especializada")
        String recommendations
) {
}
