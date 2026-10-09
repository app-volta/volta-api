package com.volta.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

@Schema(description = "Dados para cadastro de área")
public record AreaRequestDTO(

        @Schema(description = "ID da empresa dona da área", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
        @NotNull
        UUID companyId,

        @Schema(description = "Nome do setor", example = "Almoxarifado")
        @NotBlank(message = "O nome do setor é obrigatório")
        @Size(max = 100)
        String sectorName,

        @Schema(description = "Descrição da localização do setor", example = "Bloco B, térreo")
        @Size(max = 255)
        String locationDescription
) {
}
