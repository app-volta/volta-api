package com.volta.api.dto.request;

import com.volta.api.enums.RiskLevel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record WasteTypeRequestDTO(

        @NotBlank(message = "A categoria é obrigatória")
        @Size(max = 100)
        String category,

        String description,

        @NotNull(message = "O nível de risco é obrigatório")
        RiskLevel defaultRiskLevel
) {
}
