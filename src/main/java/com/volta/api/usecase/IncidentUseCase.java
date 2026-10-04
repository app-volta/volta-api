package com.volta.api.usecase;

import com.volta.api.dto.response.AiReportResponseDTO;
import com.volta.api.dto.response.IncidentResponseDTO;
import com.volta.api.dto.request.IncidentFilterDTO;
import com.volta.api.dto.request.IncidentRequestDTO;
import com.volta.api.security.AuthenticatedUser;

import java.util.List;
import java.util.UUID;

public interface IncidentUseCase {
    IncidentResponseDTO register(IncidentRequestDTO request, AuthenticatedUser author);

    List<IncidentResponseDTO> getIncidents(IncidentFilterDTO filter, AuthenticatedUser author);

    IncidentResponseDTO getIncidentById(UUID id, AuthenticatedUser author);

    void closeIncident(UUID id, AuthenticatedUser author);

    AiReportResponseDTO getAiReport(UUID id, AuthenticatedUser author);
}
