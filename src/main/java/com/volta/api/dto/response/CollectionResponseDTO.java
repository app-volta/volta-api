package com.volta.api.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record CollectionResponseDTO(
        UUID id,
        UUID incidentId,
        UUID cooperativeId,
        LocalDateTime requestedAt,
        LocalDateTime scheduledAt,
        String currentStatus,
        String collectionType,
        boolean urgent
) {
}
