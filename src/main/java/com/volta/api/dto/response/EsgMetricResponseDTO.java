package com.volta.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Métrica ESG mensal da empresa")
public record EsgMetricResponseDTO(
        @Schema(description = "ID da métrica", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
        UUID id,
        @Schema(description = "ID da empresa", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
        UUID companyId,
        @Schema(description = "Período de referência (YYYY-MM)", example = "2026-09")
        String period,
        @Schema(description = "Total de resíduos, em kg", example = "1500.00")
        BigDecimal totalWasteKg,
        @Schema(description = "Total reciclado, em kg", example = "1200.00")
        BigDecimal totalRecycledKg,
        @Schema(description = "Percentual reciclado", example = "80.00")
        BigDecimal recyclingPercentage,
        @Schema(description = "Data e hora do cálculo", example = "2026-10-15T09:30:00")
        LocalDateTime calculatedAt
) {
}
