package com.volta.api.dto.scoring;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Coordenadas geográficas")
public record CoordinatesScoringDTO(
        @Schema(description = "Latitude", example = "-23.561684")
        BigDecimal latitude,
        @Schema(description = "Longitude", example = "-46.655981")
        BigDecimal longitude
) {
}
