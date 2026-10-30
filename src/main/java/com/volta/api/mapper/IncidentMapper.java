package com.volta.api.mapper;

import com.volta.api.database.entity.Area;
import com.volta.api.database.entity.Company;
import com.volta.api.database.entity.Incident;
import com.volta.api.database.entity.Users;
import com.volta.api.database.entity.WasteType;
import com.volta.api.dto.incident.IncidentRequestDTO;
import com.volta.api.dto.incident.IncidentResponseDTO;
import com.volta.api.dto.incident.IncidentUpdateRequestDTO;
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
        incident.setContaminationLevel(
                dto.contaminationLevel() != null ? dto.contaminationLevel().name() : null
        );
        incident.setEstimatedQuantity(dto.estimatedQuantity());
        incident.setPriority(dto.priority().name());
        incident.setStatus(IncidentStatus.PENDING.name());

        return incident;
    }

    public void updateEntity(Incident incident, IncidentUpdateRequestDTO dto, WasteType wasteType) {
        if (dto.title() != null) {
            incident.setTitle(dto.title());
        }
        if (wasteType != null) {
            incident.setWasteType(wasteType);
        }
        if (dto.aiDescription() != null) {
            incident.setAiDescription(dto.aiDescription());
        }
        if (dto.contaminationLevel() != null) {
            incident.setContaminationLevel(dto.contaminationLevel().name());
        }
        if (dto.estimatedQuantity() != null) {
            incident.setEstimatedQuantity(dto.estimatedQuantity());
        }
        if (dto.priority() != null) {
            incident.setPriority(dto.priority().name());
        }
        incident.setChangeReason(dto.changeReason());
    }

    public IncidentResponseDTO toResponse(Incident incident) {
        return new IncidentResponseDTO(
                incident.getId(),
                incident.getCompany().getId(),
                incident.getUser().getId(),
                incident.getArea().getId(),
                incident.getWasteType() != null ? incident.getWasteType().getId() : null,
                incident.getPhotoUrl(),
                incident.getEmployeeDescription(),
                incident.getContaminationLevel(),
                incident.getEstimatedQuantity(),
                incident.getPriority(),
                incident.getStatus(),
                incident.getRegisteredAt(),
                incident.getTitle(),
                incident.getAiDescription(),
                incident.getChangeReason()
        );
    }
}