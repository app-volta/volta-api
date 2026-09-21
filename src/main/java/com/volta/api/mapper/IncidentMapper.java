package com.volta.api.mapper;

import com.volta.api.database.entity.Area;
import com.volta.api.database.entity.Company;
import com.volta.api.database.entity.Incident;
import com.volta.api.database.entity.Users;
import com.volta.api.database.entity.WasteType;
import com.volta.api.dto.request.IncidentRequestDTO;
import com.volta.api.dto.response.IncidentResponseDTO;
import com.volta.api.enums.IncidentStatus;
import org.springframework.stereotype.Component;

@Component
public class IncidentMapper {

    public Incident toEntity(
            IncidentRequestDTO dto,
            Company company,
            Users user,
            Area area,
            WasteType wasteType
    ) {
        Incident incident = new Incident();

        incident.setCompany(company);
        incident.setUser(user);
        incident.setArea(area);
        incident.setWasteType(wasteType);
        incident.setPhotoUrl(dto.photoUrl());
        incident.setEmployeeDescription(dto.employeeDescription());
        incident.setContaminationLevel(dto.contaminationLevel());
        incident.setEstimatedQuantity(dto.estimatedQuantity());
        incident.setPriority(dto.priority());
        incident.setStatus(IncidentStatus.PENDING.name());

        return incident;
    }

    public IncidentResponseDTO toResponse(Incident incident) {
        return new IncidentResponseDTO(
                incident.getCompany().getId(),
                incident.getUser().getId(),
                incident.getArea().getId(),
                incident.getWasteType().getId(),
                incident.getPhotoUrl(),
                incident.getEmployeeDescription(),
                incident.getContaminationLevel(),
                incident.getEstimatedQuantity(),
                incident.getPriority(),
                incident.getStatus(),
                incident.getRegisteredAt()
        );
    }
}