package com.volta.api.dto.request.update;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados para atualização de área")
public record AreaUpdateRequestDTO(

        @Schema(description = "Nome do setor", example = "Almoxarifado")
        @NotBlank(message = "O nome do setor é obrigatório")
        @Size(max = 100)
        String sectorName,

        @Schema(description = "Descrição da localização do setor", example = "Bloco B, térreo")
        @Size(max = 255)
        String locationDescription
) {
}
