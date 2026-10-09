package com.volta.api.controller.docs;

import com.volta.api.dto.request.AreaRequestDTO;
import com.volta.api.dto.request.update.AreaUpdateRequestDTO;
import com.volta.api.dto.response.AreaResponseDTO;
import com.volta.api.security.AuthenticatedUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

@Tag(name = "Áreas", description = "Setores físicos das empresas onde os incidentes são registrados")
public interface AreaControllerDocs {

    @Operation(summary = "Cadastra uma área", description = "**Acesso:** ADMIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Área cadastrada"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Empresa não encontrada")
    })
    ResponseEntity<AreaResponseDTO> create(AreaRequestDTO dto);

    @Operation(summary = "Lista todas as áreas", description = "**Acesso:** ADMIN.")
    @ApiResponse(responseCode = "200", description = "Lista de áreas")
    ResponseEntity<List<AreaResponseDTO>> show();

    @Operation(summary = "Busca uma área pelo ID", description = "**Acesso:** ADMIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Área encontrada"),
            @ApiResponse(responseCode = "404", description = "Área não encontrada")
    })
    ResponseEntity<AreaResponseDTO> show(@Parameter(description = "ID da área") UUID id);

    @Operation(
            summary = "Lista as áreas da minha empresa",
            description = "Retorna as áreas da empresa do usuário autenticado. **Acesso:** EMPLOYEE."
    )
    @ApiResponse(responseCode = "200", description = "Lista de áreas da empresa")
    ResponseEntity<List<AreaResponseDTO>> showMine(@Parameter(hidden = true) AuthenticatedUser author);

    @Operation(summary = "Atualiza uma área", description = "**Acesso:** ADMIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Área atualizada"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Área não encontrada")
    })
    ResponseEntity<AreaResponseDTO> update(@Parameter(description = "ID da área") UUID id, AreaUpdateRequestDTO dto);

    @Operation(summary = "Remove uma área", description = "**Acesso:** ADMIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Área removida"),
            @ApiResponse(responseCode = "404", description = "Área não encontrada"),
            @ApiResponse(responseCode = "409", description = "Área possui registros vinculados")
    })
    ResponseEntity<Void> remove(@Parameter(description = "ID da área") UUID id);
}
