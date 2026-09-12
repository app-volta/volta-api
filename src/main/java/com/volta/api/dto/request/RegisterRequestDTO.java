package com.volta.api.dto.request;

import lombok.Builder;

import java.util.UUID;

@Builder
public record RegisterRequestDTO (
        String name,
        String email,
        String password,
        String position,
        UUID companyId
){}
