package com.volta.api.controller.docs;

import com.volta.api.dto.summary.SummaryResponseDTO;
import com.volta.api.security.AuthenticatedUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Sumário", description = "Dados agregados para montar telas do aplicativo mobile")
public interface SummaryControllerDocs {

    @Operation(
            summary = "Consulta o sumário da empresa",
            description = "Retorna em uma única chamada os incidentes, as áreas, os tipos de resíduo e as métricas ESG "
                    + "da empresa do usuário autenticado. **Acesso:** ADMIN, MANAGER, EMPLOYEE."
    )
    @ApiResponse(responseCode = "200", description = "Sumário da empresa")
    SummaryResponseDTO showSummary(@Parameter(hidden = true) AuthenticatedUser author);
}
