package com.volta.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados da avaliação da coleta")
public record ReviewRequestDTO(

        @Schema(description = "Nota de 1 a 5", example = "5")
        @NotNull
        @Min(1)
        @Max(5)
        Integer stars,

        @Schema(description = "Comentário sobre o serviço da cooperativa", example = "Coleta pontual e equipe muito organizada")
        @Size(max = 2000)
        String comment
) {
}
