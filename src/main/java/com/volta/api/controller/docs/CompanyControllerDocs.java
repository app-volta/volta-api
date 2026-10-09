package com.volta.api.controller.docs;

import com.volta.api.dto.request.CompanyRequestDTO;
import com.volta.api.dto.response.CompanyResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

@Tag(name = "Empresas", description = "Empresas clientes que geram resíduos")
public interface CompanyControllerDocs {

    @Operation(summary = "Cadastra uma empresa", description = "**Acesso:** ADMIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Empresa cadastrada"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "CNPJ já cadastrado")
    })
    ResponseEntity<CompanyResponseDTO> create(CompanyRequestDTO dto);

    @Operation(summary = "Lista todas as empresas", description = "**Acesso:** ADMIN.")
    @ApiResponse(responseCode = "200", description = "Lista de empresas")
    ResponseEntity<List<CompanyResponseDTO>> show();

    @Operation(summary = "Busca uma empresa pelo ID", description = "**Acesso:** ADMIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Empresa encontrada"),
            @ApiResponse(responseCode = "404", description = "Empresa não encontrada")
    })
    ResponseEntity<CompanyResponseDTO> show(@Parameter(description = "ID da empresa") UUID id);

    @Operation(summary = "Atualiza uma empresa", description = "**Acesso:** ADMIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Empresa atualizada"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Empresa não encontrada"),
            @ApiResponse(responseCode = "409", description = "CNPJ já cadastrado em outra empresa")
    })
    ResponseEntity<CompanyResponseDTO> update(@Parameter(description = "ID da empresa") UUID id, CompanyRequestDTO dto);

    @Operation(summary = "Remove uma empresa", description = "**Acesso:** ADMIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Empresa removida"),
            @ApiResponse(responseCode = "404", description = "Empresa não encontrada"),
            @ApiResponse(responseCode = "409", description = "Empresa possui registros vinculados")
    })
    ResponseEntity<Void> remove(@Parameter(description = "ID da empresa") UUID id);
}
