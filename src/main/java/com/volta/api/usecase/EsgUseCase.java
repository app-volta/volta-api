package com.volta.api.usecase;

import com.volta.api.dto.esg.EsgMetricRequestDTO;
import com.volta.api.dto.esg.RecyclingPercentageRequestDTO;
import com.volta.api.dto.esg.EsgMetricResponseDTO;
import com.volta.api.dto.esg.EsgScoreResponseDTO;
import com.volta.api.dto.esg.RecyclingPercentageResponseDTO;
import com.volta.api.security.AuthenticatedUser;

import java.util.List;

public interface EsgUseCase {

    RecyclingPercentageResponseDTO calculateRecyclingPercentage(RecyclingPercentageRequestDTO dto);

    EsgScoreResponseDTO getCompanyScore(AuthenticatedUser author);

    EsgMetricResponseDTO registerMetric(EsgMetricRequestDTO dto, AuthenticatedUser author);

    List<EsgMetricResponseDTO> getMetrics(AuthenticatedUser author);
}
