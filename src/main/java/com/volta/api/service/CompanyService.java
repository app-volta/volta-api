package com.volta.api.service;

import com.volta.api.database.entity.Company;
import com.volta.api.database.repository.CompanyRepository;
import com.volta.api.dto.request.CompanyRequestDTO;
import com.volta.api.dto.response.CompanyResponseDTO;
import com.volta.api.mapper.CompanyMapper;
import com.volta.api.security.AuthenticatedUser;
import com.volta.api.usecase.CompanyUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CompanyService implements CompanyUseCase {

    private final CompanyRepository companyRepository;
    private final CompanyMapper companyMapper;

    public CompanyResponseDTO register(CompanyRequestDTO dto){
        Company company = companyMapper.toEntity(dto);
        Company companySaved = companyRepository.save(company);
        return companyMapper.toResponse(companySaved);
    }

    public List<CompanyResponseDTO> getCompanies(){
        List<CompanyResponseDTO> companies = new ArrayList<>();

        for (Company company : companyRepository.findAll()){
            companies.add(companyMapper.toResponse(company));
        }

        return companies;
    }

    public CompanyResponseDTO getCompanyById(UUID id){
        Company company = companyRepository.findById(id).orElse(null);
        return companyMapper.toResponse(company);
    }
}
