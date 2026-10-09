package com.volta.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Coleta")
public record CollectionResponseDTO(
        @Schema(description = "ID da coleta", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
        UUID id,
        @Schema(description = "ID do incidente", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
        UUID incidentId,
        @Schema(description = "ID da cooperativa", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
        UUID cooperativeId,
        @Schema(description = "Data e hora da solicitação", example = "2026-10-15T09:30:00")
        LocalDateTime requestedAt,
        @Schema(description = "Data e hora agendadas", example = "2026-10-15T09:30:00")
        LocalDateTime scheduledAt,
        @Schema(description = "Status atual", example = "SCHEDULED", allowableValues = {"REQUESTED", "SCHEDULED", "IN_PROGRESS", "COMPLETED", "CANCELED"})
        String currentStatus,
        @Schema(description = "Tipo da coleta", example = "Reciclável")
        String collectionType,
        @Schema(description = "Indica se a coleta é urgente", example = "false")
        boolean urgent
) {
}
