package com.volta.api.service;

import com.volta.api.database.entity.Cooperative;
import com.volta.api.database.repository.CooperativeRepository;
import com.volta.api.dto.request.CooperativeRequestDTO;
import com.volta.api.dto.response.CooperativeResponseDTO;
import com.volta.api.mapper.CooperativeMapper;
import com.volta.api.usecase.CooperativeUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CooperativeService implements CooperativeUseCase {

    private final CooperativeRepository cooperativeRepository;

    private final CooperativeMapper cooperativeMapper;

    public CooperativeResponseDTO register(CooperativeRequestDTO dto){
        Cooperative cooperative = cooperativeMapper.toEntity(dto);
        Cooperative cooperativeSaved = cooperativeRepository.save(cooperative);
        return cooperativeMapper.toResponse(cooperativeSaved);
    }

    public List<CooperativeResponseDTO> getCooperatives(){
        List<CooperativeResponseDTO> cooperatives = new ArrayList<>();

        for (Cooperative cooperative : cooperativeRepository.findAll()) {
            cooperatives.add(cooperativeMapper.toResponse(cooperative));
        }

        return cooperatives;
    }

    public CooperativeResponseDTO getCooperativeById(UUID id){
        Cooperative cooperative = cooperativeRepository.findById(id).orElse(null);
        return cooperativeMapper.toResponse(cooperative);
    }
}
