package com.volta.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

@Schema(description = "Dados para cadastro ou atualização de cooperativa")
public record CooperativeRequestDTO(

        @Schema(description = "Nome da cooperativa", example = "Cooperativa Recicla Mais")
        @NotBlank(message = "O nome é obrigatório")
        @Size(max = 150)
        String name,

        @Schema(description = "CNPJ, com ou sem máscara", example = "98.765.432/0001-10")
        @NotBlank(message = "O CNPJ é obrigatório")
        @Pattern(regexp = "\\b\\d{2}\\.?\\d{3}\\.?\\d{3}/?\\d{4}-?\\d{2}\\b")
        String cnpj,

        @Schema(description = "Latitude da sede", example = "-23.561684")
        @NotNull
        @DecimalMin("-90.0")
        @DecimalMax("90.0")
        @Digits(integer = 3, fraction = 6)
        BigDecimal latitude,

        @Schema(description = "Longitude da sede", example = "-46.655981")
        @NotNull
        @DecimalMin("-180.0")
        @DecimalMax("180.0")
        @Digits(integer = 3, fraction = 6)
        BigDecimal longitude,

        @Schema(description = "Tipos de resíduo em que a cooperativa é especializada", example = "Plástico, papel e metal")
        @Size(max = 500)
        String specialties
) {
    public CooperativeRequestDTO {
        if (cnpj != null) {
            cnpj = cnpj.replaceAll("\\D", "");
        }
    }
}
