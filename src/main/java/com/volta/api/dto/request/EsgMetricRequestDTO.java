package com.volta.api.dto.request;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record EsgMetricRequestDTO(

        @NotBlank
        @Pattern(regexp = "^\\d{4}-(0[1-9]|1[0-2])$", message = "Period must be in the format YYYY-MM")
        String period,

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
