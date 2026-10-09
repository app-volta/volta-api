package com.volta.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Registro do histórico de status da coleta")
public record CollectionStatusResponseDTO(

        @Schema(description = "ID do registro", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
        UUID id,
        @Schema(description = "ID da coleta", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
        UUID collectionId,
        @Schema(description = "Status registrado", example = "IN_PROGRESS", allowableValues = {"REQUESTED", "SCHEDULED", "IN_PROGRESS", "COMPLETED", "CANCELED"})
        String status,
        @Schema(description = "Data e hora da mudança", example = "2026-10-15T09:30:00")
        LocalDateTime changedAt,
        @Schema(description = "Observação da mudança", example = "Equipe a caminho do local")
        String observation
) {
}
