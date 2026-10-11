package com.volta.api.config;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "swagger")
public record SwaggerProperties(
        @NotBlank(message = "SWAGGER_USERNAME must be set")
        String username,
        @NotBlank(message = "SWAGGER_PASSWORD must be set")
        String password
) {
}
