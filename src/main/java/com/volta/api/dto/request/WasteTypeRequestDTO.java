package com.volta.api.dto.request;

import com.volta.api.enums.RiskLevel;
import com.volta.api.enums.WasteCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Dados para cadastro ou atualização de tipo de resíduo")
public record WasteTypeRequestDTO(

        @Schema(
                description = "Categoria da coleta seletiva (CONAMA 275/2001). "
                        + "PERIGOSO, SAUDE e RADIOATIVO exigem nível de risco HIGH",
                example = "PERIGOSO"
        )
        @NotNull(message = "A categoria é obrigatória")
        WasteCategory category,

        @Schema(description = "Descrição do tipo de resíduo", example = "Óleo usado em máquinas industriais")
        String description,

        @Schema(description = "Nível de risco padrão", example = "HIGH")
        @NotNull(message = "O nível de risco é obrigatório")
        RiskLevel defaultRiskLevel
) {
}
