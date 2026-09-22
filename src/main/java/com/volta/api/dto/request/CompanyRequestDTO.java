package com.volta.api.dto.request;

public record CompanyRequestDTO(
        String name,
        String cnpj,
        String address
) {
}
