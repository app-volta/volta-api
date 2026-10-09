package com.volta.api.dto.request;

import com.volta.api.enums.WasteCategory;
import com.volta.api.validation.Cnpj;
import com.volta.api.validation.WasteCategories;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.stream.Collectors;

@Schema(description = "Dados para cadastro ou atualização de cooperativa")
public record CooperativeRequestDTO(

        @Schema(description = "Nome da cooperativa", example = "Cooperativa Recicla Mais")
        @NotBlank(message = "O nome é obrigatório")
        @Size(max = 150)
        String name,

        @Schema(
                description = "CNPJ numérico ou alfanumérico, com ou sem máscara. Os dígitos verificadores são validados",
                example = "11.444.777/0001-61"
        )
        @NotBlank(message = "O CNPJ é obrigatório")
        @Cnpj
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

        @Schema(
                description = "Categorias de resíduo (CONAMA 275/2001) que a cooperativa recebe, separadas por vírgula. "
                        + "Para receber resíduo perigoso (Classe I) é preciso incluir PERIGOSO. "
                        + "Valores: PAPEL, PLASTICO, VIDRO, METAL, MADEIRA, PERIGOSO, SAUDE, RADIOATIVO, ORGANICO, NAO_RECICLAVEL",
                example = "PAPEL,PLASTICO,METAL"
        )
        @NotBlank(message = "Informe ao menos uma especialidade")
        @Size(max = 500)
        @WasteCategories
        String specialties
) {
    public CooperativeRequestDTO {
        if (cnpj != null) {
            cnpj = cnpj.replaceAll("[^0-9A-Za-z]", "").toUpperCase();
        }
        if (specialties != null) {
            specialties = normalizeSpecialties(specialties);
        }
    }

    // "papel, Plástico" -> "PAPEL,PLASTICO"
    private static String normalizeSpecialties(String specialties) {
        return Arrays.stream(specialties.split(","))
                .map(WasteCategory::normalize)
                .filter(item -> !item.isEmpty())
                .distinct()
                .collect(Collectors.joining(","));
    }
}
