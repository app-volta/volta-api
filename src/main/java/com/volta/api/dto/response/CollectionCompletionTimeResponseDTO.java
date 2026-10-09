package com.volta.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.UUID;

@Schema(description = "Tempo de conclusão da coleta")
public record CollectionCompletionTimeResponseDTO(
        @Schema(description = "ID da coleta", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
        UUID collectionId,
        @Schema(description = "Horas entre a solicitação e a conclusão", example = "36.50")
        BigDecimal completionHours
) {
}
