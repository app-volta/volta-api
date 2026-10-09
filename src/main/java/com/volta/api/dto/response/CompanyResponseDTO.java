package com.volta.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

@Schema(description = "Empresa")
public record CompanyResponseDTO(

        @Schema(description = "ID da empresa", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
        UUID id,
        @Schema(description = "Nome da empresa", example = "Volta Indústria LTDA")
        String name,
        @Schema(description = "CNPJ sem máscara", example = "11222333000181")
        String cnpj,
        @Schema(description = "Endereço", example = "Av. Paulista, 1000 - São Paulo/SP")
        String address
) {
}
