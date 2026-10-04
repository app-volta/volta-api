package com.volta.api.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

public record CooperativeResponseDTO(

        UUID id,
        String name,
        String cnpj,
        BigDecimal latitude,
        BigDecimal longitude,
        BigDecimal averageRating,
        String specialties
) {
}
