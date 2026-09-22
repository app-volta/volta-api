package com.volta.api.dto.response;

import java.util.UUID;

public record AreaResponseDTO(
        UUID companyId,
        String sectorName,
        String locationDescription
) {
}