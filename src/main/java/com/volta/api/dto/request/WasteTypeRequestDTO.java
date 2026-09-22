package com.volta.api.dto.request;

public record WasteTypeRequestDTO(
        String category,
        String description,
        String defaultRiskLevel
) {
}
