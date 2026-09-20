package com.volta.api.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record IncidentResponseDTO(
        UUID companyId,
        UUID userId,
        UUID areaId,
        UUID wasteTypeId,
        String photoUrl,
        String employeeDescription,
        String contaminationLevel,
        BigDecimal estimatedQuantity,
        String priority,
        String status,
        LocalDateTime registeredAt
        ) {
}
