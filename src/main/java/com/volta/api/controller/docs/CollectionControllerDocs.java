package com.volta.api.controller.docs;

import com.volta.api.dto.request.CollectionRequestDTO;
import com.volta.api.dto.request.CollectionScheduleRequestDTO;
import com.volta.api.dto.request.CollectionStatusRequestDTO;
import com.volta.api.dto.response.CollectionCompletionTimeResponseDTO;
import com.volta.api.dto.response.CollectionResponseDTO;
import com.volta.api.dto.response.CollectionStatusResponseDTO;
import com.volta.api.security.AuthenticatedUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

@Tag(name = "Coletas", description = "Solicitação, agendamento e acompanhamento de coletas feitas por cooperativas")
public interface CollectionControllerDocs {

    @Operation(
            summary = "Solicita uma coleta",
            description = "Cria uma coleta para um incidente da empresa do usuário. A coleta nasce com status REQUESTED.\n\n"
                    + "**Regras:**\n"
                    + "- O incidente não pode estar encerrado e só pode ter uma coleta ativa por vez;\n"
                    + "- A cooperativa precisa ter a categoria do resíduo nas especialidades (CONAMA 275/2001);\n"
                    + "- Resíduo perigoso (NBR 10004 Classe I) exige cooperativa com a especialidade PERIGOSO;\n"
                    + "- Resíduo RADIOATIVO não pode ser destinado a cooperativas;\n"
                    + "- Incidente CRITICAL gera coleta urgente.\n\n"
                    + "**Acesso:** ADMIN, MANAGER."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Coleta solicitada"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Incidente ou cooperativa não encontrados"),
            @ApiResponse(responseCode = "409", description = "Incidente já possui uma coleta ativa"),
            @ApiResponse(responseCode = "422", description = "Incidente encerrado ou cooperativa incompatível com o resíduo")
    })
    ResponseEntity<CollectionResponseDTO> create(CollectionRequestDTO dto, @Parameter(hidden = true) AuthenticatedUser author);

    @Operation(
            summary = "Lista as coletas da empresa",
            description = "Retorna as coletas da empresa do usuário autenticado. **Acesso:** ADMIN, MANAGER."
    )
    @ApiResponse(responseCode = "200", description = "Lista de coletas")
    ResponseEntity<List<CollectionResponseDTO>> show(@Parameter(hidden = true) AuthenticatedUser author);

    @Operation(summary = "Busca uma coleta pelo ID", description = "**Acesso:** ADMIN, MANAGER.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Coleta encontrada"),
            @ApiResponse(responseCode = "404", description = "Coleta não encontrada")
    })
    ResponseEntity<CollectionResponseDTO> showById(
            @Parameter(description = "ID da coleta") UUID id,
            @Parameter(hidden = true) AuthenticatedUser author
    );

    @Operation(
            summary = "Consulta o tempo de conclusão da coleta",
            description = "Retorna em horas o tempo entre a solicitação e a conclusão. "
                    + "Disponível apenas para coletas COMPLETED. **Acesso:** ADMIN, MANAGER."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tempo de conclusão calculado"),
            @ApiResponse(responseCode = "404", description = "Coleta não encontrada"),
            @ApiResponse(responseCode = "422", description = "Coleta ainda não foi concluída")
    })
    ResponseEntity<CollectionCompletionTimeResponseDTO> showCompletionTime(
            @Parameter(description = "ID da coleta") UUID id,
            @Parameter(hidden = true) AuthenticatedUser author
    );

    @Operation(
            summary = "Agenda uma coleta",
            description = "Define a data da coleta e muda o status para SCHEDULED. Também permite reagendar.\n\n"
                    + "**Prazo máximo, contado da solicitação:** urgente ou CRITICAL 24 h, HIGH 72 h, MEDIUM 7 dias, LOW 15 dias. "
                    + "Se o prazo já venceu, a coleta deve ser agendada em até 24 h.\n\n"
                    + "**Acesso:** ADMIN, MANAGER."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Coleta agendada"),
            @ApiResponse(responseCode = "400", description = "Data inválida ou no passado"),
            @ApiResponse(responseCode = "404", description = "Coleta não encontrada"),
            @ApiResponse(responseCode = "422", description = "Status atual não permite agendamento ou data fora do prazo")
    })
    ResponseEntity<Void> schedule(
            @Parameter(description = "ID da coleta") UUID id,
            CollectionScheduleRequestDTO dto,
            @Parameter(hidden = true) AuthenticatedUser author
    );

    @Operation(
            summary = "Atualiza o status de uma coleta",
            description = "Registra um novo status no histórico da coleta. Para agendar, use `PATCH /collections/{id}/schedule`.\n\n"
                    + "**Transições permitidas:** REQUESTED → SCHEDULED/CANCELED; SCHEDULED → IN_PROGRESS/CANCELED; "
                    + "IN_PROGRESS → COMPLETED. COMPLETED e CANCELED são finais.\n\n"
                    + "Cancelar exige observação. Concluir a coleta encerra automaticamente o incidente.\n\n"
                    + "**Acesso:** ADMIN, MANAGER."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Status atualizado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou cancelamento sem observação"),
            @ApiResponse(responseCode = "404", description = "Coleta não encontrada"),
            @ApiResponse(responseCode = "422", description = "Transição de status não permitida")
    })
    ResponseEntity<Void> updateStatus(
            @Parameter(description = "ID da coleta") UUID id,
            CollectionStatusRequestDTO dto,
            @Parameter(hidden = true) AuthenticatedUser author
    );

    @Operation(
            summary = "Consulta o histórico de status da coleta",
            description = "Retorna as mudanças de status em ordem cronológica. **Acesso:** ADMIN, MANAGER."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Histórico da coleta"),
            @ApiResponse(responseCode = "404", description = "Coleta não encontrada")
    })
    ResponseEntity<List<CollectionStatusResponseDTO>> show(
            @Parameter(description = "ID da coleta") UUID id,
            @Parameter(hidden = true) AuthenticatedUser author
    );
}
