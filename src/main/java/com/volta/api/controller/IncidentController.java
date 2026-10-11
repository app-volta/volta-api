package com.volta.api.controller;

import com.volta.api.controller.docs.IncidentControllerDocs;
import com.volta.api.dto.incident.IncidentFilterDTO;
import com.volta.api.dto.incident.IncidentRequestDTO;
import com.volta.api.dto.report.AiReportResponseDTO;
import com.volta.api.dto.incident.IncidentResponseDTO;
import com.volta.api.security.AuthenticatedUser;
import com.volta.api.usecase.IncidentUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequiredArgsConstructor
@RequestMapping("/incidents")
public class IncidentController implements IncidentControllerDocs {

    private final IncidentUseCase incidentUseCase;

    @PostMapping
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    @ResponseStatus(HttpStatus.CREATED)
    public IncidentResponseDTO create(
            @Valid @RequestBody IncidentRequestDTO dto,
            @AuthenticationPrincipal AuthenticatedUser author
    ) {
        return incidentUseCase.register(dto, author);
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER', 'EMPLOYEE')")
    public List<IncidentResponseDTO> show(
            @ModelAttribute IncidentFilterDTO filter,
            @AuthenticationPrincipal AuthenticatedUser author
    ) {
        List<IncidentResponseDTO> incidents = incidentUseCase.getIncidents(filter, author);
        return incidents;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER', 'EMPLOYEE')")
    public IncidentResponseDTO show(
            @PathVariable UUID id,
            @AuthenticationPrincipal AuthenticatedUser author
    ) {
        IncidentResponseDTO incident = incidentUseCase.getIncidentById(id, author);
        return incident;
    }

    @PatchMapping("/{id}/close")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void closeIncident(
            @PathVariable UUID id,
            @AuthenticationPrincipal AuthenticatedUser author
    ) {
        incidentUseCase.closeIncident(id, author);
    }

    @GetMapping("/{id}/ai-report")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER', 'EMPLOYEE')")
    public AiReportResponseDTO showAiReport(
            @PathVariable UUID id,
            @AuthenticationPrincipal AuthenticatedUser author
    ) {
        AiReportResponseDTO aiReport = incidentUseCase.getAiReport(id, author);
        return aiReport;
    }
}
