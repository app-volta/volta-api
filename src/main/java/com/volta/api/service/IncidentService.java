package com.volta.api.service;

import com.volta.api.database.entity.*;
import com.volta.api.database.procedure.IncidentProcedure;
import com.volta.api.database.repository.*;
import com.volta.api.dto.request.IncidentFilterDTO;
import com.volta.api.dto.request.IncidentRequestDTO;
import com.volta.api.dto.response.AiReportResponseDTO;
import com.volta.api.dto.response.IncidentResponseDTO;
import com.volta.api.enums.IncidentStatus;
import com.volta.api.exception.BusinessRuleException;
import com.volta.api.exception.ResourceNotFoundException;
import com.volta.api.mapper.AiReportMapper;
import com.volta.api.mapper.IncidentMapper;
import com.volta.api.security.AuthenticatedUser;
import com.volta.api.usecase.IncidentUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private final IncidentProcedure incidentProcedure;

    private final AiReportRepository aiReportRepository;

    private final AiReportMapper aiReportMapper;

    public IncidentResponseDTO register(IncidentRequestDTO dto, AuthenticatedUser author) {

        Company company = companyRepository.findById(author.companyId())
                .orElseThrow(() -> new ResourceNotFoundException("Company"));
        Users user = userRepository.findById(author.id())
                .orElseThrow(() -> new ResourceNotFoundException("User"));
        Area area = areaRepository.findByIdAndCompanyId(dto.areaId(), author.companyId()).
                orElseThrow(() -> new ResourceNotFoundException("Area"));
        WasteType wasteType = wasteTypeRepository.findById(dto.wasteTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Waste type"));

        Incident incident = incidentMapper.toEntity(
                dto,
                company,
                user,
                area,
                wasteType
        );

        Incident savedIncident = incidentRepository.saveAndFlush(incident);
        return incidentMapper.toResponse(savedIncident);
    }

    public List<IncidentResponseDTO> getIncidents(IncidentFilterDTO filter, AuthenticatedUser author) {
        List<IncidentResponseDTO> incidents = new ArrayList<>();

        List<Incident> found = incidentRepository.search(
                author.companyId(),
                filter.status(),
                filter.priority(),
                filter.areaId()
        );

        for (Incident incident : found) {
            incidents.add(incidentMapper.toResponse(incident));
        }

        return incidents;
    }

    public IncidentResponseDTO getIncidentById(UUID id, AuthenticatedUser author) {
        Incident incident = incidentRepository.findByIdAndCompanyId(id, author.companyId())
                .orElseThrow(() -> new ResourceNotFoundException("Incident"));
        return incidentMapper.toResponse(incident);
    }

    @Transactional
    public void closeIncident(UUID id, AuthenticatedUser author) {
        Incident incident = incidentRepository.findByIdAndCompanyId(id, author.companyId())
                .orElseThrow(() -> new ResourceNotFoundException("Incident"));

        if (IncidentStatus.CLOSED.name().equals(incident.getStatus())) {
            throw new BusinessRuleException("Incident is already closed");
        }

        incidentProcedure.closeIncident(id);
    }

    public AiReportResponseDTO getAiReport(UUID id, AuthenticatedUser author) {
        incidentRepository.findByIdAndCompanyId(id, author.companyId())
                .orElseThrow(() -> new ResourceNotFoundException("Incident"));

        AiReport aiReport = aiReportRepository.findFirstByIncidentIdOrderByGeneratedAtDesc(id)
                .orElseThrow(() -> new ResourceNotFoundException("AI report"));

        return aiReportMapper.toResponse(aiReport);
    }
}
