package com.volta.api.mapper;

import com.volta.api.database.entity.AiReport;
import com.volta.api.database.entity.Cooperative;
import com.volta.api.database.entity.Incident;
import com.volta.api.database.entity.WasteType;
import com.volta.api.dto.scoring.AiReportScoringDTO;
import com.volta.api.dto.scoring.CooperativeScoringDataDTO;
import com.volta.api.dto.scoring.IncidentScoringContextDTO;
import com.volta.api.dto.scoring.WasteTypeScoringDTO;
import com.volta.api.enums.WasteCategory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ScoringMapper {

    public IncidentScoringContextDTO toIncidentContext(Incident incident, AiReport aiReport) {
        WasteType wasteType = incident.getWasteType();
        String contaminationLevel = incident.getContaminationLevel();

        if (contaminationLevel == null) {
            contaminationLevel = wasteType.getDefaultRiskLevel();
        }

        AiReportScoringDTO aiReportDto = null;

        if (aiReport != null) {
            aiReportDto = new AiReportScoringDTO(
                    aiReport.getDetectedWasteType(),
                    aiReport.getAiContaminationLevel(),
                    aiReport.getRecommendations()
            );
        }

        return new IncidentScoringContextDTO(
                incident.getId(),
                incident.getPriority(),
                new WasteTypeScoringDTO(
                        WasteCategory.normalize(wasteType.getCategory()),
                        wasteType.getDefaultRiskLevel()
                ),
                contaminationLevel,
                null,
                aiReportDto
        );
    }

    public CooperativeScoringDataDTO toCooperativeData(Cooperative cooperative) {
        return new CooperativeScoringDataDTO(
                cooperative.getId(),
                cooperative.getName(),
                cooperative.getLatitude(),
                cooperative.getLongitude(),
                cooperative.getAverageRating(),
                toSpecialties(cooperative.getSpecialties())
        );
    }

    private List<String> toSpecialties(String specialties) {
        List<String> normalized = new ArrayList<>();

        if (specialties == null) {
            return normalized;
        }

        for (String specialty : specialties.split(",")) {
            String category = WasteCategory.normalize(specialty);

            if (!category.isEmpty() && !normalized.contains(category)) {
                normalized.add(category);
            }
        }

        return normalized;
    }
}
