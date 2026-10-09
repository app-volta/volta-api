package com.volta.api.dto.request;

import com.volta.api.enums.RiskLevel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados para cadastro ou atualização de tipo de resíduo")
public record WasteTypeRequestDTO(

        @Schema(description = "Categoria do resíduo", example = "Óleo lubrificante")
        @NotBlank(message = "A categoria é obrigatória")
        @Size(max = 100)
        String category,

        @Schema(description = "Descrição do tipo de resíduo", example = "Óleo usado em máquinas industriais")
        String description,

        @Schema(description = "Nível de risco padrão", example = "HIGH")
        @NotNull(message = "O nível de risco é obrigatório")
        RiskLevel defaultRiskLevel
) {
}
