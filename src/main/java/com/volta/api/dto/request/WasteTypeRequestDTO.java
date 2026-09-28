package com.volta.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record WasteTypeRequestDTO(

        @NotBlank
        @Size(max = 100)
        String category,

        String description,

        @NotBlank
        @Size(max = 50)
        String defaultRiskLevel
) {
}
