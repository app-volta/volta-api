package com.volta.api.service;

import com.volta.api.database.entity.Company;
import com.volta.api.database.entity.EsgMetric;
import com.volta.api.database.function.EsgFunction;
import com.volta.api.database.repository.CompanyRepository;
import com.volta.api.database.repository.EsgMetricRepository;
import com.volta.api.dto.request.EsgMetricRequestDTO;
import com.volta.api.dto.request.RecyclingPercentageRequestDTO;
import com.volta.api.dto.response.EsgMetricResponseDTO;
import com.volta.api.dto.response.EsgScoreResponseDTO;
import com.volta.api.dto.response.RecyclingPercentageResponseDTO;
import com.volta.api.exception.BusinessRuleException;
import com.volta.api.exception.ConflictException;
import com.volta.api.exception.ResourceNotFoundException;
import com.volta.api.mapper.EsgMetricMapper;
import com.volta.api.security.AuthenticatedUser;
import com.volta.api.usecase.EsgUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EsgService implements EsgUseCase {

    private final EsgFunction esgFunction;
    private final EsgMetricRepository esgMetricRepository;
    private final CompanyRepository companyRepository;
    private final EsgMetricMapper esgMetricMapper;

    public RecyclingPercentageResponseDTO calculateRecyclingPercentage(RecyclingPercentageRequestDTO dto) {
        validateRecycledNotGreaterThanWaste(dto.totalWasteKg(), dto.totalRecycledKg());

        BigDecimal percentage = esgFunction.calculateRecyclingPercentage(dto.totalWasteKg(), dto.totalRecycledKg());

        return new RecyclingPercentageResponseDTO(dto.totalWasteKg(), dto.totalRecycledKg(), percentage);
    }

    public EsgScoreResponseDTO getCompanyScore(AuthenticatedUser author) {
        BigDecimal score = esgFunction.calculateCompanyEsgScore(author.companyId());
        return new EsgScoreResponseDTO(author.companyId(), score);
    }

    @Transactional
    public EsgMetricResponseDTO registerMetric(EsgMetricRequestDTO dto, AuthenticatedUser author) {
        validateRecycledNotGreaterThanWaste(dto.totalWasteKg(), dto.totalRecycledKg());

        if (esgMetricRepository.existsByCompanyIdAndPeriod(author.companyId(), dto.period())) {
            throw new ConflictException("ESG metric already registered for this period");
        }

        Company company = companyRepository.findById(author.companyId())
                .orElseThrow(() -> new ResourceNotFoundException("Company"));

        BigDecimal percentage = esgFunction.calculateRecyclingPercentage(dto.totalWasteKg(), dto.totalRecycledKg());

        EsgMetric esgMetric = esgMetricMapper.toEntity(dto, company, percentage);
        EsgMetric saved = esgMetricRepository.save(esgMetric);

        return esgMetricMapper.toResponse(saved);
    }

    public List<EsgMetricResponseDTO> getMetrics(AuthenticatedUser author) {
        List<EsgMetricResponseDTO> metrics = new ArrayList<>();

        for (EsgMetric esgMetric : esgMetricRepository.findByCompanyIdOrderByCalculatedAtDesc(author.companyId())) {
            metrics.add(esgMetricMapper.toResponse(esgMetric));
        }

        return metrics;
    }

    private void validateRecycledNotGreaterThanWaste(BigDecimal totalWaste, BigDecimal totalRecycled) {
        if (totalRecycled.compareTo(totalWaste) > 0) {
            throw new BusinessRuleException("Total recycled cannot be greater than total waste");
        }
    }
}
