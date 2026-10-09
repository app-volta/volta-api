package com.volta.api.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Volta API",
                version = "v1",
                description = "API do Volta para gestão de incidentes de resíduos, coletas com cooperativas e indicadores ESG. "
                        + "Para acessar os endpoints protegidos, faça login em `POST /auth/login`, copie o token retornado "
                        + "e informe-o no botão **Authorize**."
        ),
        security = @SecurityRequirement(name = OpenApiConfiguration.BEARER_AUTH)
)
@SecurityScheme(
        name = OpenApiConfiguration.BEARER_AUTH,
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        description = "Token JWT obtido em `POST /auth/login`"
)
public class OpenApiConfiguration {

    public static final String BEARER_AUTH = "bearerAuth";
    private static final String PROBLEM_DETAIL = "ProblemDetail";
    private static final String PROBLEM_JSON = "application/problem+json";

    @Bean
    public OpenApiCustomizer globalResponsesCustomizer() {
        return openApi -> {
            openApi.getComponents().addSchemas(PROBLEM_DETAIL, problemDetailSchema());

            openApi.getPaths().values().forEach(pathItem ->
                    pathItem.readOperations().forEach(this::addDefaultResponses)
            );
        };
    }

    private void addDefaultResponses(Operation operation) {
        ApiResponses responses = operation.getResponses();

        if (isSecured(operation)) {
            responses.putIfAbsent("401", new ApiResponse().description("Token ausente, inválido ou expirado"));
            responses.putIfAbsent("403", new ApiResponse().description("Usuário sem permissão para acessar este recurso"));
        }
        responses.putIfAbsent("500", new ApiResponse().description("Erro interno do servidor"));

        // Todas as respostas de erro da API seguem o formato ProblemDetail
        responses.forEach((code, response) -> {
            if (code.startsWith("4") || code.startsWith("5")) {
                response.setContent(problemDetailContent());
            }
        });
    }

    private boolean isSecured(Operation operation) {
        return operation.getSecurity() == null || !operation.getSecurity().isEmpty();
    }

    private Content problemDetailContent() {
        return new Content().addMediaType(
                PROBLEM_JSON,
                new MediaType().schema(new Schema<>().$ref("#/components/schemas/" + PROBLEM_DETAIL))
        );
    }

    private Schema<?> problemDetailSchema() {
        return new ObjectSchema()
                .description("Formato padrão de erro da API (RFC 9457)")
                .addProperty("type", new StringSchema().example("about:blank"))
                .addProperty("title", new StringSchema().example("Not Found"))
                .addProperty("status", new IntegerSchema().example(404))
                .addProperty("detail", new StringSchema().example("Collection not found"))
                .addProperty("instance", new StringSchema().example("/collections/3fa85f64-5717-4562-b3fc-2c963f66afa6"))
                .addProperty("errors", new ObjectSchema()
                        .description("Erros por campo. Presente apenas em erros de validação (400)")
                        .additionalProperties(new StringSchema())
                        .example(Map.of("email", "O e-mail é obrigatório")));
    }
}
