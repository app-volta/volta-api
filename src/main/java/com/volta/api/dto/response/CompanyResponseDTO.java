package com.volta.api.dto.response;

import java.util.UUID;

public record CompanyResponseDTO(

        UUID id,
        String name,
        String cnpj,
        String address
) {
}
