package com.volta.api.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record CollectionStatusResponseDTO(

        UUID id,
        UUID collectionId,
        String status,
        LocalDateTime changedAt,
        String observation
) {}
