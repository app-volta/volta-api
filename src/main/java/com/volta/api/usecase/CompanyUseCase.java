package com.volta.api.usecase;

import com.volta.api.dto.request.CompanyRequestDTO;
import com.volta.api.dto.response.CompanyResponseDTO;

import java.util.List;
import java.util.UUID;

public interface CompanyUseCase {

    CompanyResponseDTO register(CompanyRequestDTO dto);

    List<CompanyResponseDTO> getCompanies();

    CompanyResponseDTO getCompanyById(UUID id);

}