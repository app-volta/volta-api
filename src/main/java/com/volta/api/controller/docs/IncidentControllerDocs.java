package com.volta.api.controller.docs;

import com.volta.api.dto.request.IncidentFilterDTO;
import com.volta.api.dto.request.IncidentRequestDTO;
import com.volta.api.dto.response.AiReportResponseDTO;
import com.volta.api.dto.response.IncidentResponseDTO;
import com.volta.api.security.AuthenticatedUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

@Tag(name = "Incidentes", description = "Registro e acompanhamento de incidentes com resíduos")
public interface IncidentControllerDocs {

    @Operation(
            summary = "Registra um incidente",
            description = "Registra um incidente em uma área da empresa do funcionário. O incidente nasce com status PENDING.\n\n"
                    + "**Regras:** sem nível de contaminação informado, assume o risco padrão do tipo de resíduo. "
                    + "Se o resíduo é perigoso ou a contaminação é HIGH, a prioridade é elevada para no mínimo HIGH.\n\n"
                    + "**Acesso:** EMPLOYEE."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Incidente registrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Área ou tipo de resíduo não encontrados")
    })
    ResponseEntity<IncidentResponseDTO> create(IncidentRequestDTO dto, @Parameter(hidden = true) AuthenticatedUser author);

    @Operation(
            summary = "Lista os incidentes da empresa",
            description = "Retorna os incidentes da empresa do usuário. Todos os filtros são opcionais. "
                    + "**Acesso:** ADMIN, MANAGER, EMPLOYEE."
    )
    @ApiResponse(responseCode = "200", description = "Lista de incidentes")
    ResponseEntity<List<IncidentResponseDTO>> show(
            @ParameterObject IncidentFilterDTO filter,
            @Parameter(hidden = true) AuthenticatedUser author
    );

    @Operation(summary = "Busca um incidente pelo ID", description = "**Acesso:** ADMIN, MANAGER, EMPLOYEE.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Incidente encontrado"),
            @ApiResponse(responseCode = "404", description = "Incidente não encontrado")
    })
    ResponseEntity<IncidentResponseDTO> show(
            @Parameter(description = "ID do incidente") UUID id,
            @Parameter(hidden = true) AuthenticatedUser author
    );

    @Operation(
            summary = "Encerra um incidente",
            description = "Muda o status do incidente para CLOSED.\n\n"
                    + "**Regras:** não é possível encerrar com coleta em andamento. Incidente com resíduo perigoso só pode ser "
                    + "encerrado depois de uma coleta concluída (PNRS, art. 27, §1º).\n\n"
                    + "**Acesso:** ADMIN, MANAGER."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Incidente encerrado"),
            @ApiResponse(responseCode = "404", description = "Incidente não encontrado"),
            @ApiResponse(responseCode = "422", description = "Incidente já encerrado, com coleta ativa ou resíduo perigoso sem coleta concluída")
    })
    ResponseEntity<Void> closeIncident(
            @Parameter(description = "ID do incidente") UUID id,
            @Parameter(hidden = true) AuthenticatedUser author
    );

    @Operation(
            summary = "Consulta o relatório de IA do incidente",
            description = "Retorna o relatório mais recente gerado pela IA para o incidente. **Acesso:** ADMIN, MANAGER, EMPLOYEE."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Relatório encontrado"),
            @ApiResponse(responseCode = "404", description = "Incidente ou relatório não encontrados")
    })
    ResponseEntity<AiReportResponseDTO> showAiReport(
            @Parameter(description = "ID do incidente") UUID id,
            @Parameter(hidden = true) AuthenticatedUser author
    );
}
