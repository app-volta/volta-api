package com.volta.api.usecase;

import com.volta.api.dto.area.AreaRequestDTO;
import com.volta.api.dto.area.AreaUpdateRequestDTO;
import com.volta.api.dto.area.AreaResponseDTO;
import com.volta.api.security.AuthenticatedUser;

import java.util.List;
import java.util.UUID;

public interface AreaUseCase {

    AreaResponseDTO register(AreaRequestDTO dto);

    List<AreaResponseDTO> getAreas();

    AreaResponseDTO getAreaById(UUID id);

    List<AreaResponseDTO> getAreasOfMyCompany(AuthenticatedUser author);

    AreaResponseDTO update(UUID id, AreaUpdateRequestDTO dto);

    void delete(UUID id);
}
