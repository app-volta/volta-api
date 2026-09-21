package com.volta.api.service;

import com.volta.api.database.entity.*;
import com.volta.api.database.repository.*;
import com.volta.api.dto.request.IncidentRequestDTO;
import com.volta.api.dto.response.IncidentResponseDTO;
import com.volta.api.mapper.IncidentMapper;
import com.volta.api.security.AuthenticatedUser;
import com.volta.api.usecase.IncidentUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class IncidentService implements IncidentUseCase {

    private final IncidentRepository incidentRepository;

    private final CompanyRepository companyRepository;

    private final UserRepository userRepository;

    private final AreaRepository areaRepository;

    private final WasteTypeRepository wasteTypeRepository;

    private final IncidentMapper incidentMapper;

    public IncidentResponseDTO register(IncidentRequestDTO dto, AuthenticatedUser author){

        Company company = companyRepository.findById(author.companyId()).orElse(null);
        Users user = userRepository.findById(author.id()).orElse(null);
        Area area = areaRepository.findById(dto.areaId()).orElse(null);
        WasteType wasteType = wasteTypeRepository.findById(dto.wasteTypeId()).orElse(null);

        Incident incident = incidentMapper.toEntity(
                dto,
                company,
                user,
                area,
                wasteType
        );

        Incident savedIncident = incidentRepository.save(incident);
        return incidentMapper.toResponse(savedIncident);
    }

    public List<IncidentResponseDTO> getIncidents(AuthenticatedUser author){
        List<IncidentResponseDTO> incidents = new ArrayList<>();

        for (Incident incident : incidentRepository.findByCompanyId(author.companyId())){
            incidents.add(incidentMapper.toResponse(incident));
        }

        return incidents;
    }

    public IncidentResponseDTO getIncidentById(UUID id, AuthenticatedUser author){
        Incident incident = incidentRepository.findByIdAndCompanyId(id, author.companyId()).orElse(null);
        return incidentMapper.toResponse(incident);
    }
}