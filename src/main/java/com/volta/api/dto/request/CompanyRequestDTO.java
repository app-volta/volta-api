package com.volta.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CompanyRequestDTO(

        @NotBlank
        @Size(max = 150)
        String name,

        @NotBlank
        @Pattern(regexp = "\\b\\d{2}\\.?\\d{3}\\.?\\d{3}/?\\d{4}-?\\d{2}\\b")
        String cnpj,

        @NotBlank
        @Size(max = 255)
        String address
) {
}
