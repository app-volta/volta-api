package com.volta.api.dto.response;

import java.math.BigDecimal;

public record CooperativeResponseDTO(
        String name,
        String cnpj,
        BigDecimal latitude,
        BigDecimal longitude,
        BigDecimal averageRating,
        String specialties
) {
}
