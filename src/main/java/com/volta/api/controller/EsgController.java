package com.volta.api.controller;

import com.volta.api.controller.docs.EsgControllerDocs;
import com.volta.api.dto.esg.EsgMetricRequestDTO;
import com.volta.api.dto.esg.RecyclingPercentageRequestDTO;
import com.volta.api.dto.esg.EsgMetricResponseDTO;
import com.volta.api.dto.esg.EsgScoreResponseDTO;
import com.volta.api.dto.esg.RecyclingPercentageResponseDTO;
import com.volta.api.security.AuthenticatedUser;
import com.volta.api.usecase.EsgUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/esg")
public class EsgController implements EsgControllerDocs {

    private final EsgUseCase esgUseCase;

    @PostMapping("/recycling-percentage")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    public RecyclingPercentageResponseDTO calculateRecyclingPercentage(
            @Valid @RequestBody RecyclingPercentageRequestDTO dto
    ) {
        return esgUseCase.calculateRecyclingPercentage(dto);
    }

    @GetMapping("/score")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    public EsgScoreResponseDTO showScore(@AuthenticationPrincipal AuthenticatedUser author) {
        EsgScoreResponseDTO score = esgUseCase.getCompanyScore(author);
        return score;
    }

    @PostMapping("/metrics")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER', 'EMPlOYEE')")
    @ResponseStatus(HttpStatus.CREATED)
    public EsgMetricResponseDTO createMetric(
            @Valid @RequestBody EsgMetricRequestDTO dto,
            @AuthenticationPrincipal AuthenticatedUser author
    ) {
        return esgUseCase.registerMetric(dto, author);
    }

    @GetMapping("/metrics")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER', 'EMPLOYEE')")
    public List<EsgMetricResponseDTO> showMetrics(@AuthenticationPrincipal AuthenticatedUser author) {
        List<EsgMetricResponseDTO> metrics = esgUseCase.getMetrics(author);
        return metrics;
    }
}
