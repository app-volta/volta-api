package com.volta.api.dto.request;

import com.volta.api.validation.Cnpj;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados para cadastro ou atualização de empresa")
public record CompanyRequestDTO(

        @Schema(description = "Razão social ou nome da empresa", example = "Volta Indústria LTDA")
        @NotBlank(message = "O nome é obrigatório")
        @Size(max = 150)
        String name,

        @Schema(
                description = "CNPJ numérico ou alfanumérico, com ou sem máscara. Os dígitos verificadores são validados",
                example = "11.222.333/0001-81"
        )
        @NotBlank(message = "O CNPJ é obrigatório")
        @Cnpj
        String cnpj,

        @Schema(description = "Endereço da empresa", example = "Av. Paulista, 1000 - São Paulo/SP")
        @NotBlank(message = "O endereço é obrigatório")
        @Size(max = 255)
        String address
) {
    public CompanyRequestDTO {
        if (cnpj != null) {
            cnpj = cnpj.replaceAll("[^0-9A-Za-z]", "").toUpperCase();
        }

    }
}
