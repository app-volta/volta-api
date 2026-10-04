package com.volta.api.service;

import com.volta.api.database.entity.WasteType;
import com.volta.api.database.repository.WasteTypeRepository;
import com.volta.api.dto.request.WasteTypeRequestDTO;
import com.volta.api.dto.response.WasteTypeResponseDTO;
import com.volta.api.exception.ResourceNotFoundException;
import com.volta.api.mapper.WasteTypeMapper;
import com.volta.api.usecase.WasteTypeUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WasteTypeService implements WasteTypeUseCase {

    private final WasteTypeRepository wasteTypeRepository;

    private final WasteTypeMapper wasteTypeMapper;

    public WasteTypeResponseDTO register(WasteTypeRequestDTO dto) {
        WasteType wasteType = wasteTypeMapper.toEntity(dto);
        WasteType wasteTypeSaved = wasteTypeRepository.save(wasteType);
        return wasteTypeMapper.toResponse(wasteTypeSaved);
    }

    public List<WasteTypeResponseDTO> getWasteTypes() {
        List<WasteTypeResponseDTO> wasteTypes = new ArrayList<>();

        for (WasteType wasteType : wasteTypeRepository.findAll()) {
            wasteTypes.add(wasteTypeMapper.toResponse(wasteType));
        }

        return wasteTypes;
    }

    public WasteTypeResponseDTO getWasteTypeById(UUID id) {
        WasteType wasteType = wasteTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Waste type"));
        return wasteTypeMapper.toResponse(wasteType);
    }

    @Transactional
    public WasteTypeResponseDTO update(UUID id, WasteTypeRequestDTO dto) {
        WasteType wasteType = wasteTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Waste type"));
        wasteTypeMapper.updateEntity(wasteType, dto);
        wasteTypeRepository.save(wasteType);

        return wasteTypeMapper.toResponse(wasteType);
    }

    public void delete(UUID id) {
        if (!wasteTypeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Waste type");
        }
        wasteTypeRepository.deleteById(id);
    }
}
