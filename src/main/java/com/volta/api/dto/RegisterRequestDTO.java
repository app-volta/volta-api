package com.volta.api.dto;

import lombok.Builder;

@Builder
public record RegisterRequestDTO (
        String name,
        String email,
        String password
){}
