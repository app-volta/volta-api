package com.volta.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record AreaRequestDTO(

        @NotNull
        UUID companyId,

        @NotBlank(message = "O nome do setor é obrigatório")
        @Size(max = 100)
        String sectorName,

        @Size(max = 255)
        String locationDescription
) {
}
