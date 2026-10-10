package com.volta.api.support;

import com.volta.api.database.entity.WasteType;
import com.volta.api.dto.request.WasteTypeRequestDTO;
import com.volta.api.enums.RiskLevel;
import com.volta.api.enums.WasteCategory;

import java.util.UUID;

public final class WasteTypeFixtures {

    private WasteTypeFixtures() {
    }

    public static WasteTypeRequestDTO request(WasteCategory category, RiskLevel riskLevel) {
        return new WasteTypeRequestDTO(category, "Test description", riskLevel);
    }

    public static WasteType entity(UUID id, WasteCategory category, RiskLevel riskLevel) {
        WasteType wasteType = new WasteType();
        wasteType.setId(id);
        wasteType.setCategory(category.name());
        wasteType.setDescription("Test description");
        wasteType.setDefaultRiskLevel(riskLevel.name());
        return wasteType;
    }
}
