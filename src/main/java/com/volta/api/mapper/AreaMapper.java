package com.volta.api.mapper;

import com.volta.api.database.entity.Area;
import com.volta.api.database.entity.Company;
import com.volta.api.dto.request.AreaRequestDTO;
import com.volta.api.dto.response.AreaResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class AreaMapper {
    public Area toEntity(AreaRequestDTO dto, Company company){
        Area area = new Area();

        area.setCompany(company);
        area.setSectorName(dto.sectorName());
        area.setLocationDescription(dto.locationDescription());

        return area;
    }
    public AreaResponseDTO toResponse(Area area){
        return new AreaResponseDTO(
            area.getCompany().getId(),
            area.getSectorName(),
            area.getLocationDescription()
        );
    }
}
