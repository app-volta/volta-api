package com.volta.api.dto.request.update;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserUpdateRequestDTO(

        @NotBlank(message = "O nome é obrigatório")
        @Size(max = 150)
        String name,

        @Size(max = 100)
        String position
) {
}
