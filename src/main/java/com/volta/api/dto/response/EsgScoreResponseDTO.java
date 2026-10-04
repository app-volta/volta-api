package com.volta.api.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

public record EsgScoreResponseDTO(
        UUID companyId,
        BigDecimal score
) {
}
