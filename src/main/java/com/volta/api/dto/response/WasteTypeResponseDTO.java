package com.volta.api.dto.response;

import java.util.UUID;

public record WasteTypeResponseDTO(

        UUID id,
        String category,
        String description,
        String defaultRiskLevel
) {
}
