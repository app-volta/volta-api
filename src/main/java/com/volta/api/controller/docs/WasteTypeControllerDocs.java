package com.volta.api.controller.docs;

import com.volta.api.dto.request.WasteTypeRequestDTO;
import com.volta.api.dto.response.WasteTypeResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

@Tag(name = "Tipos de resíduo", description = "Catálogo de categorias de resíduo e seu nível de risco padrão")
public interface WasteTypeControllerDocs {

    @Operation(summary = "Cadastra um tipo de resíduo", description = "**Acesso:** ADMIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Tipo de resíduo cadastrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    ResponseEntity<WasteTypeResponseDTO> create(WasteTypeRequestDTO dto);

    @Operation(summary = "Lista todos os tipos de resíduo", description = "**Acesso:** qualquer usuário autenticado.")
    @ApiResponse(responseCode = "200", description = "Lista de tipos de resíduo")
    ResponseEntity<List<WasteTypeResponseDTO>> show();

    @Operation(summary = "Busca um tipo de resíduo pelo ID", description = "**Acesso:** qualquer usuário autenticado.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tipo de resíduo encontrado"),
            @ApiResponse(responseCode = "404", description = "Tipo de resíduo não encontrado")
    })
    ResponseEntity<WasteTypeResponseDTO> show(@Parameter(description = "ID do tipo de resíduo") UUID id);

    @Operation(summary = "Atualiza um tipo de resíduo", description = "**Acesso:** ADMIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tipo de resíduo atualizado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Tipo de resíduo não encontrado")
    })
    ResponseEntity<WasteTypeResponseDTO> update(@Parameter(description = "ID do tipo de resíduo") UUID id, WasteTypeRequestDTO dto);

    @Operation(summary = "Remove um tipo de resíduo", description = "**Acesso:** ADMIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Tipo de resíduo removido"),
            @ApiResponse(responseCode = "404", description = "Tipo de resíduo não encontrado"),
            @ApiResponse(responseCode = "409", description = "Tipo de resíduo possui registros vinculados")
    })
    ResponseEntity<Void> remove(@Parameter(description = "ID do tipo de resíduo") UUID id);
}
