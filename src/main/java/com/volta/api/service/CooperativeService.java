package com.volta.api.service;

import com.volta.api.database.entity.Cooperative;
import com.volta.api.database.repository.CooperativeRepository;
import com.volta.api.dto.cooperative.CooperativeRequestDTO;
import com.volta.api.dto.cooperative.CooperativeResponseDTO;
import com.volta.api.enums.WasteCategory;
import com.volta.api.exception.ConflictException;
import com.volta.api.exception.InvalidRequestException;
import com.volta.api.exception.ResourceNotFoundException;
import com.volta.api.mapper.CooperativeMapper;
import com.volta.api.usecase.CooperativeUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CooperativeService implements CooperativeUseCase {

    private final CooperativeRepository cooperativeRepository;

    private final CooperativeMapper cooperativeMapper;


    public CooperativeResponseDTO register(CooperativeRequestDTO dto) {
        validateSpecialties(dto.specialties());

        if (cooperativeRepository.existsByCnpj(dto.cnpj())) {
            throw new ConflictException("CNPJ already registered");
        }
        Cooperative cooperative = cooperativeMapper.toEntity(dto);
        Cooperative cooperativeSaved = cooperativeRepository.save(cooperative);
        return cooperativeMapper.toResponse(cooperativeSaved);
    }

    public List<CooperativeResponseDTO> getCooperatives() {
        List<CooperativeResponseDTO> cooperatives = new ArrayList<>();

        for (Cooperative cooperative : cooperativeRepository.findAll()) {
            cooperatives.add(cooperativeMapper.toResponse(cooperative));
        }

        return cooperatives;
    }

    public CooperativeResponseDTO getCooperativeById(UUID id) {
        Cooperative cooperative = cooperativeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cooperative"));
        return cooperativeMapper.toResponse(cooperative);
    }

    @Transactional
    public CooperativeResponseDTO update(UUID id, CooperativeRequestDTO dto) {
        validateSpecialties(dto.specialties());

        Cooperative cooperative = cooperativeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cooperative"));

        if (cooperativeRepository.existsByCnpjAndIdNot(dto.cnpj(), id)) {
            throw new ConflictException("CNPJ already registered");
        }

        cooperativeMapper.updateEntity(cooperative, dto);
        cooperativeRepository.save(cooperative);
        return cooperativeMapper.toResponse(cooperative);
    }

    public void delete(UUID id) {
        if (!cooperativeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cooperative");
        }
        cooperativeRepository.deleteById(id);
    }

    private void validateSpecialties(String specialties) {
        for (String specialty : specialties.split(",")) {
            if (WasteCategory.from(specialty).isEmpty()) {
                throw new InvalidRequestException(
                        "Invalid specialty '" + specialty + "'. Accepted values: " + Arrays.toString(WasteCategory.values())
                );
            }
        }
    }
}
