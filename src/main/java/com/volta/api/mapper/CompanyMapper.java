package com.volta.api.mapper;

import com.volta.api.database.entity.Company;
import com.volta.api.dto.company.CompanyRequestDTO;
import com.volta.api.dto.company.CompanyResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class CompanyMapper {

    public Company toEntity(CompanyRequestDTO dto) {
        Company company = new Company();

        company.setName(dto.name());
        company.setCnpj(dto.cnpj());
        company.setAddress(dto.address());
        company.setLatitude(dto.latitude());
        company.setLongitude(dto.longitude());

        return company;
    }

    public CompanyResponseDTO toResponse(Company company) {
        return new CompanyResponseDTO(
                company.getId(),
                company.getName(),
                company.getCnpj(),
                company.getAddress(),
                company.getLatitude(),
                company.getLongitude()
        );
    }

    public void updateEntity(Company company, CompanyRequestDTO dto) {
        company.setName(dto.name());
        company.setCnpj(dto.cnpj());
        company.setAddress(dto.address());
        company.setLatitude(dto.latitude());
        company.setLongitude(dto.longitude());
    }
}