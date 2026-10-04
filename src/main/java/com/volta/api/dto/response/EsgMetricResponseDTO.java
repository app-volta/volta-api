package com.volta.api.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record EsgMetricResponseDTO(
        UUID id,
        UUID companyId,
        String period,
        BigDecimal totalWasteKg,
        BigDecimal totalRecycledKg,
        BigDecimal recyclingPercentage,
        LocalDateTime calculatedAt
) {}
