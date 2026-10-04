package com.volta.api.mapper;

import com.volta.api.database.entity.WasteType;
import com.volta.api.dto.request.WasteTypeRequestDTO;
import com.volta.api.dto.response.WasteTypeResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class WasteTypeMapper {

    public WasteType toEntity(WasteTypeRequestDTO dto) {
        WasteType wasteType = new WasteType();

        wasteType.setCategory(dto.category());
        wasteType.setDescription(dto.description());
        wasteType.setDefaultRiskLevel(dto.defaultRiskLevel().name());

        return wasteType;
    }

    public WasteTypeResponseDTO toResponse(WasteType wasteType) {
        return new WasteTypeResponseDTO(
                wasteType.getId(),
                wasteType.getCategory(),
                wasteType.getDescription(),
                wasteType.getDefaultRiskLevel()
        );
    }

    public void updateEntity(WasteType wasteType, WasteTypeRequestDTO dto) {
        wasteType.setCategory(dto.category());
        wasteType.setDescription(dto.description());
        wasteType.setDefaultRiskLevel(dto.defaultRiskLevel().name());
    }
}
