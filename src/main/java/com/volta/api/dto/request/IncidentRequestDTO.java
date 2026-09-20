package com.volta.api.dto.request;


import java.math.BigDecimal;
import java.util.UUID;

public record IncidentRequestDTO(
        UUID areaId,
        UUID wasteTypeId,
        String photoUrl,
        String employeeDescription,
        String contaminationLevel,
        BigDecimal estimatedQuantity,
        String priority
) {
}
