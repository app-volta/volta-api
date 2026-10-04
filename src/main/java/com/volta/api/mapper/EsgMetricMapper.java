package com.volta.api.mapper;

import com.volta.api.database.entity.Company;
import com.volta.api.database.entity.EsgMetric;
import com.volta.api.dto.request.EsgMetricRequestDTO;
import com.volta.api.dto.response.EsgMetricResponseDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class EsgMetricMapper {

    public EsgMetric toEntity(EsgMetricRequestDTO dto, Company company, BigDecimal recyclingPercentage) {
        EsgMetric esgMetric = new EsgMetric();

        esgMetric.setCompany(company);
        esgMetric.setPeriod(dto.period());
        esgMetric.setTotalWasteKg(dto.totalWasteKg());
        esgMetric.setTotalRecycledKg(dto.totalRecycledKg());
        esgMetric.setRecyclingPercentage(recyclingPercentage);

        return esgMetric;
    }

    public EsgMetricResponseDTO toResponse(EsgMetric esgMetric) {
        return new EsgMetricResponseDTO(
                esgMetric.getId(),
                esgMetric.getCompany().getId(),
                esgMetric.getPeriod(),
                esgMetric.getTotalWasteKg(),
                esgMetric.getTotalRecycledKg(),
                esgMetric.getRecyclingPercentage(),
                esgMetric.getCalculatedAt()
        );
    }
}
