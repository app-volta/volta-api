package com.volta.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.UUID;

@Schema(description = "Score ESG da empresa")
public record EsgScoreResponseDTO(
        @Schema(description = "ID da empresa", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
        UUID companyId,
        @Schema(description = "Score ESG calculado", example = "82.75")
        BigDecimal score
) {
}
