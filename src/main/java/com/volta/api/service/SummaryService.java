package com.volta.api.service;

import com.volta.api.dto.incident.IncidentFilterDTO;
import com.volta.api.dto.summary.SummaryResponseDTO;
import com.volta.api.security.AuthenticatedUser;
import com.volta.api.usecase.AreaUseCase;
import com.volta.api.usecase.EsgUseCase;
import com.volta.api.usecase.IncidentUseCase;
import com.volta.api.usecase.SummaryUseCase;
import com.volta.api.usecase.WasteTypeUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SummaryService implements SummaryUseCase {

    private final IncidentUseCase incidentUseCase;
    private final AreaUseCase areaUseCase;
    private final WasteTypeUseCase wasteTypeUseCase;
    private final EsgUseCase esgUseCase;

    @Transactional(readOnly = true)
    public SummaryResponseDTO getSummary(AuthenticatedUser author) {
        return new SummaryResponseDTO(
                incidentUseCase.getIncidents(new IncidentFilterDTO(null, null, null), author),
                areaUseCase.getAreasOfMyCompany(author),
                wasteTypeUseCase.getWasteTypes(),
                esgUseCase.getMetrics(author)
        );
    }
}
