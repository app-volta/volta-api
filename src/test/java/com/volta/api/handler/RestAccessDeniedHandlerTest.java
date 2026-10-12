package com.volta.api.handler;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.json.ProblemDetailJacksonMixin;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("RestAccessDeniedHandler")
class RestAccessDeniedHandlerTest {

    private final JsonMapper jsonMapper = JsonMapper.builder()
            .addMixIn(ProblemDetail.class, ProblemDetailJacksonMixin.class)
            .build();

    private final RestAccessDeniedHandler handler = new RestAccessDeniedHandler(jsonMapper);

    @Test
    @DisplayName("responde 403 em problem+json com a rota solicitada")
    void handle_accessDenied_writesProblemJsonWith403() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/companies");
        MockHttpServletResponse response = new MockHttpServletResponse();

        handler.handle(request, response, new AccessDeniedException("denied"));

        assertThat(response.getStatus()).isEqualTo(403);
        assertThat(response.getContentType()).startsWith("application/problem+json");
        JsonNode body = jsonMapper.readTree(response.getContentAsString());
        assertThat(body.get("status").asInt()).isEqualTo(403);
        assertThat(body.get("detail").asString()).isEqualTo("You do not have permission to access this resource");
        assertThat(body.get("instance").asString()).isEqualTo("/companies");
    }
}
