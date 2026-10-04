package com.volta.api.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

public record CollectionCompletionTimeResponseDTO(
        UUID collectionId,
        BigDecimal completionHours
) {
}
