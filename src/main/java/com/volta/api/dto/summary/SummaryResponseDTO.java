package com.volta.api.dto.summary;

import com.volta.api.dto.area.AreaResponseDTO;
import com.volta.api.dto.esg.EsgMetricResponseDTO;
import com.volta.api.dto.incident.IncidentResponseDTO;
import com.volta.api.dto.wastetype.WasteTypeResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Sumário com os dados necessários para montar a tela inicial do mobile")
public record SummaryResponseDTO(
        @Schema(description = "Incidentes da empresa do usuário")
        List<IncidentResponseDTO> incidents,
        @Schema(description = "Áreas da empresa do usuário")
        List<AreaResponseDTO> areas,
        @Schema(description = "Tipos de resíduo cadastrados")
        List<WasteTypeResponseDTO> wasteTypes,
        @Schema(description = "Métricas ESG da empresa, da mais recente para a mais antiga")
        List<EsgMetricResponseDTO> esgMetrics
) {
}
