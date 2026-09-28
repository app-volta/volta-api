package com.volta.api.dto.request;


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

        @NotBlank
        String employeeDescription,

        @Size(max = 50)
        String contaminationLevel,

        @PositiveOrZero
        @Digits(integer = 10, fraction = 2)
        BigDecimal estimatedQuantity,

        @NotBlank
        @Size(max = 30)
        String priority
) {
}
