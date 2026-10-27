package com.volta.api.usecase;

import com.volta.api.dto.cooperative.CooperativeRequestDTO;
import com.volta.api.dto.cooperative.CooperativeResponseDTO;
import com.volta.api.dto.scoring.CooperativeScoringDataDTO;

import java.util.List;
import java.util.UUID;

public interface CooperativeUseCase {

    CooperativeResponseDTO register(CooperativeRequestDTO dto);

    List<CooperativeResponseDTO> getCooperatives();

    List<CooperativeScoringDataDTO> getScoringData();

    CooperativeResponseDTO getCooperativeById(UUID id);

    CooperativeResponseDTO update(UUID id, CooperativeRequestDTO dto);

    void delete(UUID id);
}
