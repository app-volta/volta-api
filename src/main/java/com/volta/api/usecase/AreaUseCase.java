package com.volta.api.usecase;

import com.volta.api.dto.request.AreaRequestDTO;
import com.volta.api.dto.response.AreaResponseDTO;
import com.volta.api.security.AuthenticatedUser;

import java.util.UUID;

public interface AreaUseCase {

    AreaResponseDTO register(AreaRequestDTO dto, AuthenticatedUser author);

    AreaResponseDTO getAreaById(UUID id);
}
