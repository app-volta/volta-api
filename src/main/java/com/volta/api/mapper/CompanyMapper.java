package com.volta.api.mapper;

import com.volta.api.database.entity.Company;
import com.volta.api.dto.request.CompanyRequestDTO;
import com.volta.api.dto.response.CompanyResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class CompanyMapper {

    public Company toEntity(CompanyRequestDTO dto){
        Company company = new Company();

        company.setName(dto.name());
        company.setCnpj(dto.cnpj());
        company.setAddress(dto.address());

        return company;
    }

    public CompanyResponseDTO toResponse(Company company){
        return new CompanyResponseDTO(
                company.getName(),
                company.getCnpj(),
                company.getAddress()
        );
    }
}