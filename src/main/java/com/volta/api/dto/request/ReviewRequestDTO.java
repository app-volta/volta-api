package com.volta.api.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ReviewRequestDTO(

        @NotNull
        @Min(1)
        @Max(5)
        Integer stars,

        @Size(max = 2000)
        String comment
) {
}
