package com.volta.api.usecase;

import com.volta.api.dto.response.IncidentResponseDTO;
import com.volta.api.dto.request.RegisterIncidentRequestDTO;
import com.volta.api.security.AuthenticatedUser;

public interface RegisterIncidentUseCase {
    IncidentResponseDTO register(RegisterIncidentRequestDTO request, AuthenticatedUser author);
}
