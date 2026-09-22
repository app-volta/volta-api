package com.volta.api.dto.request;

import java.util.UUID;

public record AreaRequestDTO(
        String sectorName,
        String locationDescription
) {
}
