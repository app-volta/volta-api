package com.volta.api.service;

import com.volta.api.database.entity.*;
import com.volta.api.database.repository.*;
import com.volta.api.dto.request.RegisterIncidentRequestDTO;
import com.volta.api.dto.response.IncidentResponseDTO;
import com.volta.api.mapper.IncidentMapper;
import com.volta.api.security.AuthenticatedUser;
import com.volta.api.usecase.RegisterIncidentUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterIncidentService implements RegisterIncidentUseCase {

    private final IncidentRepository incidentRepository;
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final AreaRepository areaRepository;
    private final WasteTypeRepository wasteTypeRepository;
    private final IncidentMapper incidentMapper;

    public IncidentResponseDTO register(RegisterIncidentRequestDTO dto, AuthenticatedUser author){


        Company company = companyRepository.findById(dto.companyId()).orElse(null);
        Users user = userRepository.findById(dto.userId()).orElse(null);
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
}