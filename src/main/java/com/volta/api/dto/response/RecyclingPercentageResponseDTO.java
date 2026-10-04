package com.volta.api.dto.response;

import java.math.BigDecimal;

public record RecyclingPercentageResponseDTO(
        BigDecimal totalWasteKg,
        BigDecimal totalRecycledKg,
        BigDecimal recyclingPercentage
) {}
