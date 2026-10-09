package com.volta.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Schema(description = "Dados para agendamento de coleta")
public record CollectionScheduleRequestDTO(
        @Schema(description = "Data e hora agendadas (deve ser futura)", example = "2026-10-15T09:30:00")
        @NotNull
        @Future
        LocalDateTime scheduledAt
) {
}
