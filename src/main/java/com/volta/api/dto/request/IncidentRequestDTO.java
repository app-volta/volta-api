package com.volta.api.dto.request;


import com.volta.api.enums.Priority;
import com.volta.api.enums.RiskLevel;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.URL;

import java.math.BigDecimal;
import java.util.UUID;

public record IncidentRequestDTO(

        @NotNull
        UUID areaId,

        @NotNull
        UUID wasteTypeId,

        @URL
        @Size(max = 500)
        String photoUrl,

        @NotBlank(message = "A descrição é obrigatória")
        String employeeDescription,

        RiskLevel contaminationLevel,

        @PositiveOrZero
        @Digits(integer = 10, fraction = 2)
        BigDecimal estimatedQuantity,

        @NotNull(message = "A prioridade é obrigatória")
        Priority priority
) {
}
