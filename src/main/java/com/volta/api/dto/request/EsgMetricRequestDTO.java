package com.volta.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

@Schema(description = "Dados para registro de métrica ESG mensal")
public record EsgMetricRequestDTO(

        @Schema(description = "Período de referência no formato YYYY-MM", example = "2026-09")
        @NotBlank
        @Pattern(regexp = "^\\d{4}-(0[1-9]|1[0-2])$", message = "Period must be in the format YYYY-MM")
        String period,

        @Schema(description = "Total de resíduos gerados no período, em kg", example = "1500.00")
        @NotNull
        @PositiveOrZero
        @Digits(integer = 12, fraction = 2)
        BigDecimal totalWasteKg,

        @Schema(description = "Total reciclado no período, em kg (não pode ser maior que o total de resíduos)", example = "1200.00")
        @NotNull
        @PositiveOrZero
        @Digits(integer = 12, fraction = 2)
        BigDecimal totalRecycledKg
) {
}
