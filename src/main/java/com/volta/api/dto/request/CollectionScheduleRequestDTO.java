package com.volta.api.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CollectionScheduleRequestDTO(
        @NotNull
        @Future
        LocalDateTime scheduledAt
) {
}
