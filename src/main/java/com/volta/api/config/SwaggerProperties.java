package com.volta.api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "swagger")
public record SwaggerProperties(
        String username,
        String password
) {
}
