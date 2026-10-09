package com.volta.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.UUID;

@Schema(description = "Cooperativa")
public record CooperativeResponseDTO(

        @Schema(description = "ID da cooperativa", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
        UUID id,
        @Schema(description = "Nome da cooperativa", example = "Cooperativa Recicla Mais")
        String name,
        @Schema(description = "CNPJ sem máscara", example = "98765432000110")
        String cnpj,
        @Schema(description = "Latitude da sede", example = "-23.561684")
        BigDecimal latitude,
        @Schema(description = "Longitude da sede", example = "-46.655981")
        BigDecimal longitude,
        @Schema(description = "Média das avaliações recebidas (1 a 5)", example = "4.50")
        BigDecimal averageRating,
        @Schema(description = "Especialidades da cooperativa", example = "Plástico, papel e metal")
        String specialties
) {
}
