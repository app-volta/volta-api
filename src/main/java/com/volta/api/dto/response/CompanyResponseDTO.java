package com.volta.api.dto.response;

public record CompanyResponseDTO(
        String name,
        String cnpj,
        String address
) {
}
