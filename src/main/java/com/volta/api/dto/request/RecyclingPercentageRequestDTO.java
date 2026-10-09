package com.volta.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

@Schema(description = "Totais para cálculo do percentual de reciclagem")
public record RecyclingPercentageRequestDTO(

        @Schema(description = "Total de resíduos, em kg", example = "1500.00")
        @NotNull
        @PositiveOrZero
        @Digits(integer = 12, fraction = 2)
        BigDecimal totalWasteKg,
        @Schema(description = "Total reciclado, em kg (não pode ser maior que o total de resíduos)", example = "1200.00")
        @NotNull
        @PositiveOrZero
        @Digits(integer = 12, fraction = 2)
        BigDecimal totalRecycledKg
) {
}
