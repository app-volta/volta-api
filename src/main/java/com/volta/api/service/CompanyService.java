package com.volta.api.service;

import com.volta.api.database.entity.Company;
import com.volta.api.database.repository.CompanyRepository;
import com.volta.api.dto.request.CompanyRequestDTO;
import com.volta.api.dto.response.CompanyResponseDTO;
import com.volta.api.exception.ConflictException;
import com.volta.api.exception.ResourceNotFoundException;
import com.volta.api.mapper.CompanyMapper;
import com.volta.api.usecase.CompanyUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CompanyService implements CompanyUseCase {

    private final CompanyRepository companyRepository;
    private final CompanyMapper companyMapper;

    public CompanyResponseDTO register(CompanyRequestDTO dto) {
        if (companyRepository.existsByCnpj(dto.cnpj())) {
            throw new ConflictException("CNPJ already registered");
        }
        Company company = companyMapper.toEntity(dto);
        Company companySaved = companyRepository.save(company);
        return companyMapper.toResponse(companySaved);
    }

    public List<CompanyResponseDTO> getCompanies() {
        List<CompanyResponseDTO> companies = new ArrayList<>();

        for (Company company : companyRepository.findAll()) {
            companies.add(companyMapper.toResponse(company));
        }

        return companies;
    }

    public CompanyResponseDTO getCompanyById(UUID id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company"));
        return companyMapper.toResponse(company);
    }

    @Transactional
    public CompanyResponseDTO update(UUID id, CompanyRequestDTO dto) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company"));

        if (companyRepository.existsByCnpjAndIdNot(dto.cnpj(), id)) {
            throw new ConflictException("CNPJ already registered");
        }

        companyMapper.updateEntity(company, dto);
        companyRepository.save(company);

        return companyMapper.toResponse(company);
    }

    public void delete(UUID id) {
        if (!companyRepository.existsById(id)) {
            throw new ResourceNotFoundException("Company");
        }
        companyRepository.deleteById(id);
    }
}
