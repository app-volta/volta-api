package com.volta.api.database.function;

import com.volta.api.database.entity.EsgMetric;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.UUID;

public interface EsgFunction extends Repository<EsgMetric, UUID> {

    @NativeQuery("SELECT calculate_recycling_percentage(:p_total_waste, :p_total_recycled)")
    BigDecimal calculateRecyclingPercentage(
            @Param("p_total_waste") BigDecimal totalWaste,
            @Param("p_total_recycled") BigDecimal totalRecycled
    );

    @NativeQuery("SELECT calculate_company_esg_score(:p_company_id)")
    BigDecimal calculateCompanyEsgScore(@Param("p_company_id") UUID companyId);
}
