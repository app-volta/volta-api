package com.volta.api.dto.request;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record RecyclingPercentageRequestDTO(

        @NotNull
        @PositiveOrZero
        @Digits(integer = 12, fraction = 2)
        BigDecimal totalWasteKg,
        @NotNull
        @PositiveOrZero
        @Digits(integer = 12, fraction = 2)
        BigDecimal totalRecycledKg
) {
}
