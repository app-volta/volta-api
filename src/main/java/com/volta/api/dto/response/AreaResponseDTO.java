package com.volta.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

@Schema(description = "Área da empresa")
public record AreaResponseDTO(

        @Schema(description = "ID da área", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
        UUID id,
        @Schema(description = "ID da empresa", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
        UUID companyId,
        @Schema(description = "Nome do setor", example = "Almoxarifado")
        String sectorName,
        @Schema(description = "Descrição da localização", example = "Bloco B, térreo")
        String locationDescription
) {
}