package com.volta.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

@Schema(description = "Dados para solicitação de coleta")
public record CollectionRequestDTO(

        @Schema(description = "ID do incidente que originou a coleta", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
        @NotNull
        UUID incidentId,

        @Schema(description = "ID da cooperativa responsável", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
        @NotNull
        UUID cooperativeId,

        @Schema(description = "Tipo da coleta", example = "Reciclável")
        @NotBlank
        @Size(max = 50)
        String collectionType,

        @Schema(description = "Indica se a coleta é urgente", example = "false")
        boolean urgent
) {
}
