package com.volta.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(description = "Resultado do cálculo de reciclagem")
public record RecyclingPercentageResponseDTO(
        @Schema(description = "Total de resíduos, em kg", example = "1500.00")
        BigDecimal totalWasteKg,
        @Schema(description = "Total reciclado, em kg", example = "1200.00")
        BigDecimal totalRecycledKg,
        @Schema(description = "Percentual reciclado", example = "80.00")
        BigDecimal recyclingPercentage
) {
}
