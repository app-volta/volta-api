package com.volta.api.dto.company;

import com.volta.api.utils.CnpjUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CNPJ;

@Schema(description = "Dados para cadastro ou atualização de empresa")
public record CompanyRequestDTO(

        @Schema(description = "Razão social ou nome da empresa", example = "Volta Indústria LTDA")
        @NotBlank(message = "Name is required")
        @Size(max = 150)
        String name,

        @Schema(
                description = "CNPJ numérico ou alfanumérico, com ou sem máscara. Os dígitos verificadores são validados",
                example = "11.222.333/0001-81"
        )
        @NotBlank(message = "CNPJ is required")
        @CNPJ(format = CNPJ.Format.ALPHANUMERIC, message = "Invalid CNPJ")
        String cnpj,

        @Schema(description = "Endereço da empresa", example = "Av. Paulista, 1000 - São Paulo/SP")
        @NotBlank(message = "Address is required")
        @Size(max = 255)
        String address,

        @Schema(description = "Latitude da empresa; deve ser informada junto com a longitude", example = "-23.561684")
        @DecimalMin(value = "-90.0", message = "Latitude must be between -90 and 90")
        @DecimalMax(value = "90.0", message = "Latitude must be between -90 and 90")
        Double latitude,

        @Schema(description = "Longitude da empresa; deve ser informada junto com a latitude", example = "-46.655981")
        @DecimalMin(value = "-180.0", message = "Longitude must be between -180 and 180")
        @DecimalMax(value = "180.0", message = "Longitude must be between -180 and 180")
        Double longitude
) {
    public CompanyRequestDTO {
        cnpj = CnpjUtils.normalize(cnpj);
    }
}
