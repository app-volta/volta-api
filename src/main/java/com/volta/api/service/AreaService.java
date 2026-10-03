package com.volta.api.service;

import com.volta.api.database.entity.Area;
import com.volta.api.database.entity.Company;
import com.volta.api.database.repository.AreaRepository;
import com.volta.api.database.repository.CompanyRepository;
import com.volta.api.dto.request.AreaRequestDTO;
import com.volta.api.dto.request.AreaUpdateRequestDTO;
import com.volta.api.dto.response.AreaResponseDTO;
import com.volta.api.exception.ResourceNotFoundException;
import com.volta.api.mapper.AreaMapper;
import com.volta.api.security.AuthenticatedUser;
import com.volta.api.usecase.AreaUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AreaService implements AreaUseCase {
    private final AreaRepository areaRepository;
    private final CompanyRepository companyRepository;
    private final AreaMapper areaMapper;

    public AreaResponseDTO register(AreaRequestDTO dto){
        Company company = companyRepository.findById(dto.companyId())
                .orElseThrow(() -> new ResourceNotFoundException("Company"));

        Area area = areaMapper.toEntity(dto, company);

        Area savedArea = areaRepository.save(area);
        return areaMapper.toResponse(savedArea);
    }

    public List<AreaResponseDTO> getAreas(){
        List<AreaResponseDTO> areas = new ArrayList<>();

        for (Area area : areaRepository.findAll()) {
            areas.add(areaMapper.toResponse(area));
        }

        return areas;
    }

    public AreaResponseDTO getAreaById(UUID id){
        Area area = areaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Area"));
        return areaMapper.toResponse(area);
    }

    public List<AreaResponseDTO> getAreasOfMyCompany(AuthenticatedUser author){
        List<AreaResponseDTO> areas = new ArrayList<>();

        for (Area area : areaRepository.findByCompanyId(author.companyId())) {
            areas.add(areaMapper.toResponse(area));
        }

        return areas;
    }

    @Transactional
    public AreaResponseDTO update(UUID id, AreaUpdateRequestDTO dto){
        Area area = areaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Area"));

        areaMapper.updateEntity(area, dto);
        areaRepository.save(area);
        return areaMapper.toResponse(area);
    }

    public void delete(UUID id){
        if (!areaRepository.existsById(id)){
            throw new ResourceNotFoundException("Area");
        }
        areaRepository.deleteById(id);
    }
}
