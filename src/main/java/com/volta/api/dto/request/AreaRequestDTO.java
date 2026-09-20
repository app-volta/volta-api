package com.volta.api.dto.request;

import java.util.UUID;

public record AreaRequestDTO(
        UUID companyId,
        String sectorName,
        String locationDescription
) {
}
