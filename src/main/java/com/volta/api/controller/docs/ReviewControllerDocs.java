package com.volta.api.controller.docs;

import com.volta.api.dto.request.ReviewRequestDTO;
import com.volta.api.dto.response.ReviewResponseDTO;
import com.volta.api.security.AuthenticatedUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

@Tag(name = "Avaliações", description = "Avaliações das cooperativas após a conclusão das coletas")
public interface ReviewControllerDocs {

    @Operation(
            summary = "Avalia uma coleta",
            description = "Registra a avaliação da cooperativa responsável pela coleta. "
                    + "Só coletas COMPLETED podem ser avaliadas, e apenas uma vez. A nota média da cooperativa é recalculada. "
                    + "**Acesso:** ADMIN, MANAGER."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Avaliação registrada"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Coleta não encontrada"),
            @ApiResponse(responseCode = "409", description = "Coleta já foi avaliada"),
            @ApiResponse(responseCode = "422", description = "Coleta ainda não foi concluída")
    })
    ResponseEntity<ReviewResponseDTO> create(
            @Parameter(description = "ID da coleta") UUID collectionId,
            ReviewRequestDTO dto,
            @Parameter(hidden = true) AuthenticatedUser author
    );

    @Operation(summary = "Lista as avaliações de uma cooperativa", description = "**Acesso:** ADMIN, MANAGER.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de avaliações"),
            @ApiResponse(responseCode = "404", description = "Cooperativa não encontrada")
    })
    ResponseEntity<List<ReviewResponseDTO>> showByCooperative(@Parameter(description = "ID da cooperativa") UUID cooperativeId);
}
