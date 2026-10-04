package com.volta.api.usecase;

import com.volta.api.dto.request.EsgMetricRequestDTO;
import com.volta.api.dto.request.RecyclingPercentageRequestDTO;
import com.volta.api.dto.response.EsgMetricResponseDTO;
import com.volta.api.dto.response.EsgScoreResponseDTO;
import com.volta.api.dto.response.RecyclingPercentageResponseDTO;
import com.volta.api.security.AuthenticatedUser;

import java.util.List;

public interface EsgUseCase {

    RecyclingPercentageResponseDTO calculateRecyclingPercentage(RecyclingPercentageRequestDTO dto);

    EsgScoreResponseDTO getCompanyScore(AuthenticatedUser author);

    EsgMetricResponseDTO registerMetric(EsgMetricRequestDTO dto, AuthenticatedUser author);

    List<EsgMetricResponseDTO> getMetrics(AuthenticatedUser author);
}
