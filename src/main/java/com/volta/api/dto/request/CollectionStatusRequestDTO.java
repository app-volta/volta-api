package com.volta.api.dto.request;

import com.volta.api.enums.CollectionStatusType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Dados para atualização de status da coleta")
public record CollectionStatusRequestDTO(

        @Schema(description = "Novo status da coleta (SCHEDULED não é aceito, use o endpoint de agendamento)", example = "IN_PROGRESS")
        @NotNull
        CollectionStatusType status,

        @Schema(description = "Observação sobre a mudança de status. Obrigatória para CANCELED", example = "Equipe a caminho do local")
        String observation
) {
}
