package com.volta.api.usecase;

import com.volta.api.dto.request.WasteTypeRequestDTO;
import com.volta.api.dto.response.WasteTypeResponseDTO;

import java.util.List;
import java.util.UUID;

public interface WasteTypeUseCase {

    WasteTypeResponseDTO register(WasteTypeRequestDTO dto);

    List<WasteTypeResponseDTO> getWasteTypes();

    WasteTypeResponseDTO getWasteTypeById(UUID id);

}