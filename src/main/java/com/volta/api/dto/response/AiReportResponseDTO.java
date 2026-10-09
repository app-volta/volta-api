package com.volta.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Relatório gerado pela IA para um incidente")
public record AiReportResponseDTO(
        @Schema(description = "ID do relatório", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
        UUID id,
        @Schema(description = "ID do incidente analisado", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
        UUID incidentId,
        @Schema(description = "Tipo de resíduo identificado pela IA", example = "Óleo lubrificante")
        String detectedWasteType,
        @Schema(description = "Nível de contaminação estimado pela IA", example = "HIGH")
        String aiContaminationLevel,
        @Schema(description = "Recomendações de tratamento", example = "Isolar a área e acionar coleta especializada")
        String recommendations,
        @Schema(description = "Texto completo do relatório", example = "Foi identificado vazamento de óleo...")
        String reportText,
        @Schema(description = "Data e hora de geração", example = "2026-10-15T09:30:00")
        LocalDateTime generatedAt
) {
}
