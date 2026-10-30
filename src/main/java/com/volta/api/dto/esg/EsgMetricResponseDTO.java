package com.volta.api.dto.esg;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDate;
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
        LocalDateTime calculatedAt,
        @Schema(description = "Meta semanal de reciclagem, em kg", example = "300.00")
        BigDecimal weeklyTarget,
        @Schema(description = "Data da meta semanal", example = "2026-10-12")
        LocalDate weeklyDate,
        @Schema(description = "Meta mensal de reciclagem, em kg", example = "1200.00")
        BigDecimal monthlyTarget,
        @Schema(description = "Data da meta mensal", example = "2026-10-31")
        LocalDate monthlyDate,
        @Schema(description = "Meta trimestral de reciclagem, em kg", example = "3600.00")
        BigDecimal quarterlyTarget,
        @Schema(description = "Data da meta trimestral", example = "2026-12-31")
        LocalDate quarterlyDate
) {
}
