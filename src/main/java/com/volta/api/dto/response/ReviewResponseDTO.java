package com.volta.api.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record ReviewResponseDTO(
        UUID id,
        UUID cooperativeId,
        UUID userId,
        UUID collectionId,
        int stars,
        String comment,
        LocalDateTime reviewedAt
) {
}
