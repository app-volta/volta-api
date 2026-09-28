package com.volta.api.dto.request;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record CooperativeRequestDTO(

        @NotBlank
        @Size(max = 150)
        String name,

        @NotBlank
        @Pattern(regexp = "\\b\\d{2}\\.?\\d{3}\\.?\\d{3}/?\\d{4}-?\\d{2}\\b")
        String cnpj,

        @NotNull
        @DecimalMin("-90.0")
        @DecimalMax("90.0")
        @Digits(integer = 3, fraction = 6)
        BigDecimal latitude,

        @NotNull
        @DecimalMin("-180.0")
        @DecimalMax("180.0")
        @Digits(integer = 3, fraction = 6)
        BigDecimal longitude,

        @Size(max = 500)
        String specialties
) {
}
