package com.volta.api.usecase;

import com.volta.api.dto.request.CooperativeRequestDTO;
import com.volta.api.dto.response.CooperativeResponseDTO;

import java.util.List;
import java.util.UUID;

public interface CooperativeUseCase {

    CooperativeResponseDTO register(CooperativeRequestDTO dto);

    List<CooperativeResponseDTO> getCooperatives();

    CooperativeResponseDTO getCooperativeById(UUID id);

    CooperativeResponseDTO update(UUID id, CooperativeRequestDTO dto);

    void delete(UUID id);
}
