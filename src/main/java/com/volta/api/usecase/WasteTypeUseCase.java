package com.volta.api.usecase;

import com.volta.api.dto.wastetype.WasteTypeRequestDTO;
import com.volta.api.dto.wastetype.WasteTypeResponseDTO;

import java.util.List;
import java.util.UUID;

public interface WasteTypeUseCase {

    WasteTypeResponseDTO register(WasteTypeRequestDTO dto);

    List<WasteTypeResponseDTO> getWasteTypes();

    WasteTypeResponseDTO getWasteTypeById(UUID id);

    WasteTypeResponseDTO update(UUID id, WasteTypeRequestDTO dto);

    void delete(UUID id);

}