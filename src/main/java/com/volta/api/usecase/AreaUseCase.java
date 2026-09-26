package com.volta.api.usecase;

import com.volta.api.dto.request.AreaRequestDTO;
import com.volta.api.dto.response.AreaResponseDTO;
import com.volta.api.security.AuthenticatedUser;

import java.util.List;
import java.util.UUID;

public interface AreaUseCase {

    AreaResponseDTO register(AreaRequestDTO dto, AuthenticatedUser author);

    List<AreaResponseDTO> getAreas();

    AreaResponseDTO getAreaById(UUID id);

    void deleteArea(UUID id);
}
