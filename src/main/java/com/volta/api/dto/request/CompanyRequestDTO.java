package com.volta.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados para cadastro ou atualização de empresa")
public record CompanyRequestDTO(

        @Schema(description = "Razão social ou nome da empresa", example = "Volta Indústria LTDA")
        @NotBlank(message = "O nome é obrigatório")
        @Size(max = 150)
        String name,

        @Schema(description = "CNPJ, com ou sem máscara", example = "12.345.678/0001-90")
        @NotBlank(message = "O CNPJ é obrigatório")
        @Pattern(regexp = "\\b\\d{2}\\.?\\d{3}\\.?\\d{3}/?\\d{4}-?\\d{2}\\b")
        String cnpj,

        @Schema(description = "Endereço da empresa", example = "Av. Paulista, 1000 - São Paulo/SP")
        @NotBlank(message = "O endereço é obrigatório")
        @Size(max = 255)
        String address
) {
    public CompanyRequestDTO {
        if (cnpj != null) {
            cnpj = cnpj.replaceAll("\\D", "");
        }

    }
}
