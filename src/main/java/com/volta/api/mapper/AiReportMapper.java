package com.volta.api.mapper;

import com.volta.api.database.entity.AiReport;
import com.volta.api.dto.response.AiReportResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class AiReportMapper {

    public AiReportResponseDTO toResponse(AiReport aiReport) {
        return new AiReportResponseDTO(
                aiReport.getId(),
                aiReport.getIncident().getId(),
                aiReport.getDetectedWasteType(),
                aiReport.getAiContaminationLevel(),
                aiReport.getRecommendations(),
                aiReport.getReportText(),
                aiReport.getGeneratedAt()
        );
    }
}
