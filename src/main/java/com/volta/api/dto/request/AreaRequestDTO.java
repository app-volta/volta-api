package com.volta.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record AreaRequestDTO(

        @NotBlank
        @Size(max = 255)
        String sectorName,

        @Size(max = 255)
        String locationDescription
) {
}
