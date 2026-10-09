package com.volta.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Incidente")
public record IncidentResponseDTO(

        @Schema(description = "ID do incidente", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
        UUID id,
        @Schema(description = "ID da empresa", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
        UUID companyId,
        @Schema(description = "ID do funcionário que registrou", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
        UUID userId,
        @Schema(description = "ID da área", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
        UUID areaId,
        @Schema(description = "ID do tipo de resíduo", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
        UUID wasteTypeId,
        @Schema(description = "URL da foto", example = "https://storage.volta.com/incidentes/foto.jpg")
        String photoUrl,
        @Schema(description = "Descrição feita pelo funcionário", example = "Vazamento de óleo próximo à máquina 3")
        String employeeDescription,
        @Schema(description = "Nível de contaminação", example = "MEDIUM", allowableValues = {"LOW", "MEDIUM", "HIGH"})
        String contaminationLevel,
        @Schema(description = "Quantidade estimada, em kg", example = "25.50")
        BigDecimal estimatedQuantity,
        @Schema(description = "Prioridade", example = "HIGH", allowableValues = {"LOW", "MEDIUM", "HIGH", "CRITICAL"})
        String priority,
        @Schema(description = "Status do incidente", example = "PENDING", allowableValues = {"PENDING", "CLOSED"})
        String status,
        @Schema(description = "Data e hora do registro", example = "2026-10-15T09:30:00")
        LocalDateTime registeredAt
) {
}
