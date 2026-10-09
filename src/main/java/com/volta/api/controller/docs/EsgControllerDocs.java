package com.volta.api.controller.docs;

import com.volta.api.dto.request.EsgMetricRequestDTO;
import com.volta.api.dto.request.RecyclingPercentageRequestDTO;
import com.volta.api.dto.response.EsgMetricResponseDTO;
import com.volta.api.dto.response.EsgScoreResponseDTO;
import com.volta.api.dto.response.RecyclingPercentageResponseDTO;
import com.volta.api.security.AuthenticatedUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "ESG", description = "Indicadores ambientais: percentual de reciclagem, métricas mensais e score ESG")
public interface EsgControllerDocs {

    @Operation(
            summary = "Calcula o percentual de reciclagem",
            description = "Simula o percentual reciclado a partir dos totais informados, sem salvar nada. "
                    + "**Acesso:** ADMIN, MANAGER."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Percentual calculado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "422", description = "Total reciclado maior que o total de resíduos")
    })
    ResponseEntity<RecyclingPercentageResponseDTO> calculateRecyclingPercentage(RecyclingPercentageRequestDTO dto);

    @Operation(
            summary = "Consulta o score ESG da empresa",
            description = "Calcula o score ESG da empresa do usuário autenticado. **Acesso:** ADMIN, MANAGER."
    )
    @ApiResponse(responseCode = "200", description = "Score ESG da empresa")
    ResponseEntity<EsgScoreResponseDTO> showScore(@Parameter(hidden = true) AuthenticatedUser author);

    @Operation(
            summary = "Registra a métrica ESG de um período",
            description = "Salva os totais de resíduos e reciclagem de um mês para a empresa do usuário. "
                    + "Só é permitida uma métrica por período. **Acesso:** ADMIN, MANAGER."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Métrica registrada"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Empresa não encontrada"),
            @ApiResponse(responseCode = "409", description = "Já existe métrica para este período"),
            @ApiResponse(responseCode = "422", description = "Total reciclado maior que o total de resíduos")
    })
    ResponseEntity<EsgMetricResponseDTO> createMetric(EsgMetricRequestDTO dto, @Parameter(hidden = true) AuthenticatedUser author);

    @Operation(
            summary = "Lista as métricas ESG da empresa",
            description = "Retorna as métricas da empresa do usuário, da mais recente para a mais antiga. "
                    + "**Acesso:** ADMIN, MANAGER."
    )
    @ApiResponse(responseCode = "200", description = "Lista de métricas")
    ResponseEntity<List<EsgMetricResponseDTO>> showMetrics(@Parameter(hidden = true) AuthenticatedUser author);
}
