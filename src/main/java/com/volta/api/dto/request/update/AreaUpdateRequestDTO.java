package com.volta.api.dto.request.update;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AreaUpdateRequestDTO(

        @NotBlank(message = "O nome do setor é obrigatório")
        @Size(max = 100)
        String sectorName,

        @Size(max = 255)
        String locationDescription
) {}
