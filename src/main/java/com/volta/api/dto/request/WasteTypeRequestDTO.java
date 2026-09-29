package com.volta.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record WasteTypeRequestDTO(

        @NotBlank(message = "A categoria é obrigatória")
        @Size(max = 100)
        String category,

        String description,

        @NotBlank(message = "O nível de risco é obrigatório")
        @Size(max = 50)
        String defaultRiskLevel
) {
}
