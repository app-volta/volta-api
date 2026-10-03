package com.volta.api.dto.response;

import java.util.UUID;

public record UserResponseDTO(
        UUID id,
        String name,
        String email,
        String position,
        UUID companyId,
        String role

) {
}
