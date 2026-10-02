package com.volta.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CompanyRequestDTO(

        @NotBlank(message = "O nome é obrigatório")
        @Size(max = 150)
        String name,

        @NotBlank(message = "O CNPJ é obrigatório")
        @Pattern(regexp = "\\b\\d{2}\\.?\\d{3}\\.?\\d{3}/?\\d{4}-?\\d{2}\\b")
        String cnpj,

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
