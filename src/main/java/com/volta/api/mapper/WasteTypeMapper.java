package com.volta.api.mapper;

import com.volta.api.database.entity.WasteType;
import com.volta.api.dto.request.WasteTypeRequestDTO;
import com.volta.api.dto.response.WasteTypeResponseDTO;
import com.volta.api.enums.RiskLevel;
import com.volta.api.enums.WasteCategory;
import org.springframework.stereotype.Component;

@Component
public class WasteTypeMapper {

    public WasteType toEntity(WasteTypeRequestDTO dto) {
        WasteType wasteType = new WasteType();

        wasteType.setCategory(dto.category().name());
        wasteType.setDescription(dto.description());
        wasteType.setDefaultRiskLevel(dto.defaultRiskLevel().name());

        return wasteType;
    }

    public WasteTypeResponseDTO toResponse(WasteType wasteType) {
        String color = WasteCategory.from(wasteType.getCategory())
                .map(WasteCategory::getColor)
                .orElse(null);
        String wasteClass = RiskLevel.isHazardous(wasteType.getDefaultRiskLevel()) ? "CLASSE_I" : "CLASSE_II";

        return new WasteTypeResponseDTO(
                wasteType.getId(),
                wasteType.getCategory(),
                wasteType.getDescription(),
                wasteType.getDefaultRiskLevel(),
                color,
                wasteClass
        );
    }

    public void updateEntity(WasteType wasteType, WasteTypeRequestDTO dto) {
        wasteType.setCategory(dto.category().name());
        wasteType.setDescription(dto.description());
        wasteType.setDefaultRiskLevel(dto.defaultRiskLevel().name());
    }
}
