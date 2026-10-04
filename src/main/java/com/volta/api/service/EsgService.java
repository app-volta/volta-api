package com.volta.api.service;

import com.volta.api.database.function.EsgFunction;
import com.volta.api.dto.request.RecyclingPercentageRequestDTO;
import com.volta.api.dto.response.EsgScoreResponseDTO;
import com.volta.api.dto.response.RecyclingPercentageResponseDTO;
import com.volta.api.exception.BusinessRuleException;
import com.volta.api.security.AuthenticatedUser;
import com.volta.api.usecase.EsgUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class EsgService implements EsgUseCase {

    private final EsgFunction esgFunction;

    public RecyclingPercentageResponseDTO calculateRecyclingPercentage(RecyclingPercentageRequestDTO dto) {
        if (dto.totalRecycledKg().compareTo(dto.totalWasteKg()) > 0) {
            throw new BusinessRuleException("Total recycled cannot be greater than total waste");
        }

        BigDecimal percentage = esgFunction.calculateRecyclingPercentage(dto.totalWasteKg(), dto.totalRecycledKg());

        return new RecyclingPercentageResponseDTO(dto.totalWasteKg(), dto.totalRecycledKg(), percentage);
    }

    public EsgScoreResponseDTO getCompanyScore(AuthenticatedUser author) {
        BigDecimal score = esgFunction.calculateCompanyEsgScore(author.companyId());
        return new EsgScoreResponseDTO(author.companyId(), score);
    }
}
