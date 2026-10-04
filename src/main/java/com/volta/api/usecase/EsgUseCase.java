package com.volta.api.usecase;

import com.volta.api.dto.request.RecyclingPercentageRequestDTO;
import com.volta.api.dto.response.EsgScoreResponseDTO;
import com.volta.api.dto.response.RecyclingPercentageResponseDTO;
import com.volta.api.security.AuthenticatedUser;

public interface EsgUseCase {

    RecyclingPercentageResponseDTO calculateRecyclingPercentage(RecyclingPercentageRequestDTO dto);

    EsgScoreResponseDTO getCompanyScore(AuthenticatedUser author);
}
