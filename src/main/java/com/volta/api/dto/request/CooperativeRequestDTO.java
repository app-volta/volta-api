package com.volta.api.dto.request;

import java.math.BigDecimal;

public record CooperativeRequestDTO(
        String name,
        String cnpj,
        BigDecimal latitude,
        BigDecimal longitude,
        BigDecimal averageRating,
        String specialties
) {
}
