package com.volta.api.dto.response;

import java.util.UUID;

public record AreaResponseDTO(

        UUID id,
        UUID companyId,
        String sectorName,
        String locationDescription
) {
}