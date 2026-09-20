package com.volta.api.service;

import com.volta.api.database.entity.Area;
import com.volta.api.database.entity.Company;
import com.volta.api.database.repository.AreaRepository;
import com.volta.api.database.repository.CompanyRepository;
import com.volta.api.dto.request.AreaRequestDTO;
import com.volta.api.dto.response.AreaResponseDTO;
import com.volta.api.mapper.AreaMapper;
import com.volta.api.security.AuthenticatedUser;
import com.volta.api.usecase.AreaUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AreaService implements AreaUseCase {
    private final AreaRepository areaRepository;
    private final CompanyRepository companyRepository;
    private final AreaMapper areaMapper;

    public AreaResponseDTO register(AreaRequestDTO dto, AuthenticatedUser author){
        Company company = companyRepository.findById(dto.companyId()).orElse(null);

        Area area = areaMapper.toEntity(dto, company);

        Area savedArea = areaRepository.save(area);
        return areaMapper.toResponse(savedArea);
    }

    public AreaResponseDTO getAreaById(UUID id){
        Area area = areaRepository.findById(id).orElse(null);
        return areaMapper.toResponse(area);
    }
}
