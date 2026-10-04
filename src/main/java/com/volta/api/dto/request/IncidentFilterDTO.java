package com.volta.api.dto.request;

import java.util.UUID;

public record IncidentFilterDTO(
        String status,
        String priority,
        UUID areaId
) {}
