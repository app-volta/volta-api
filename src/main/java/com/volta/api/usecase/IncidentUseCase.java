package com.volta.api.usecase;

import com.volta.api.dto.response.IncidentResponseDTO;
import com.volta.api.dto.request.IncidentRequestDTO;
import com.volta.api.security.AuthenticatedUser;

import java.util.UUID;

public interface IncidentUseCase {
    IncidentResponseDTO register(IncidentRequestDTO request, AuthenticatedUser author);

    IncidentResponseDTO getIncidentById(UUID id);
}
