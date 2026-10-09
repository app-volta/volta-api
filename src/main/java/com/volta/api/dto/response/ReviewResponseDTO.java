package com.volta.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Avaliação de coleta")
public record ReviewResponseDTO(
        @Schema(description = "ID da avaliação", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
        UUID id,
        @Schema(description = "ID da cooperativa avaliada", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
        UUID cooperativeId,
        @Schema(description = "ID do usuário que avaliou", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
        UUID userId,
        @Schema(description = "ID da coleta", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
        UUID collectionId,
        @Schema(description = "Nota de 1 a 5", example = "5")
        int stars,
        @Schema(description = "Comentário", example = "Coleta pontual e equipe muito organizada")
        String comment,
        @Schema(description = "Data e hora da avaliação", example = "2026-10-15T09:30:00")
        LocalDateTime reviewedAt
) {
}
