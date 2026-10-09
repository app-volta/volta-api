package com.volta.api.controller.docs;

import com.volta.api.dto.request.CooperativeRequestDTO;
import com.volta.api.dto.response.CooperativeResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

@Tag(name = "Cooperativas", description = "Cooperativas de reciclagem responsáveis pelas coletas")
public interface CooperativeControllerDocs {

    @Operation(summary = "Cadastra uma cooperativa", description = "**Acesso:** ADMIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Cooperativa cadastrada"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "CNPJ já cadastrado")
    })
    ResponseEntity<CooperativeResponseDTO> create(CooperativeRequestDTO dto);

    @Operation(summary = "Lista todas as cooperativas", description = "**Acesso:** ADMIN.")
    @ApiResponse(responseCode = "200", description = "Lista de cooperativas")
    ResponseEntity<List<CooperativeResponseDTO>> show();

    @Operation(summary = "Busca uma cooperativa pelo ID", description = "**Acesso:** ADMIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cooperativa encontrada"),
            @ApiResponse(responseCode = "404", description = "Cooperativa não encontrada")
    })
    ResponseEntity<CooperativeResponseDTO> show(@Parameter(description = "ID da cooperativa") UUID id);

    @Operation(summary = "Atualiza uma cooperativa", description = "**Acesso:** ADMIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cooperativa atualizada"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Cooperativa não encontrada"),
            @ApiResponse(responseCode = "409", description = "CNPJ já cadastrado em outra cooperativa")
    })
    ResponseEntity<CooperativeResponseDTO> update(@Parameter(description = "ID da cooperativa") UUID id, CooperativeRequestDTO dto);

    @Operation(summary = "Remove uma cooperativa", description = "**Acesso:** ADMIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Cooperativa removida"),
            @ApiResponse(responseCode = "404", description = "Cooperativa não encontrada"),
            @ApiResponse(responseCode = "409", description = "Cooperativa possui registros vinculados")
    })
    ResponseEntity<Void> remove(@Parameter(description = "ID da cooperativa") UUID id);
}
