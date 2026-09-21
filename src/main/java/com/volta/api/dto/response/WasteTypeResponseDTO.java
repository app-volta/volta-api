package com.volta.api.dto.response;

public record WasteTypeResponseDTO(
        String category,
        String description,
        String defaultRiskLevel
) {
}
