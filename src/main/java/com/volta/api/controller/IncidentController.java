package com.volta.api.controller;

import com.volta.api.controller.docs.IncidentControllerDocs;
import com.volta.api.dto.request.IncidentFilterDTO;
import com.volta.api.dto.request.IncidentRequestDTO;
import com.volta.api.dto.response.AiReportResponseDTO;
import com.volta.api.dto.response.IncidentResponseDTO;
import com.volta.api.security.AuthenticatedUser;

import com.volta.api.usecase.IncidentUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<IncidentResponseDTO> create(
            @Valid @RequestBody IncidentRequestDTO dto,
            @AuthenticationPrincipal AuthenticatedUser author
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(incidentUseCase.register(dto, author));
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER', 'EMPLOYEE')")
    public ResponseEntity<List<IncidentResponseDTO>> show(
            @ModelAttribute IncidentFilterDTO filter,
            @AuthenticationPrincipal AuthenticatedUser author
    ) {
        List<IncidentResponseDTO> incidents = incidentUseCase.getIncidents(filter, author);
        return ResponseEntity.ok(incidents);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER', 'EMPLOYEE')")
    public ResponseEntity<IncidentResponseDTO> show(
            @PathVariable UUID id,
            @AuthenticationPrincipal AuthenticatedUser author
    ) {
        IncidentResponseDTO incident = incidentUseCase.getIncidentById(id, author);
        return ResponseEntity.ok(incident);
    }

    @PatchMapping("/{id}/close")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    public ResponseEntity<Void> closeIncident(
            @PathVariable UUID id,
            @AuthenticationPrincipal AuthenticatedUser author
    ) {
        incidentUseCase.closeIncident(id, author);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/ai-report")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER', 'EMPLOYEE')")
    public ResponseEntity<AiReportResponseDTO> showAiReport(
            @PathVariable UUID id,
            @AuthenticationPrincipal AuthenticatedUser author
    ) {
        AiReportResponseDTO aiReport = incidentUseCase.getAiReport(id, author);
        return ResponseEntity.ok(aiReport);
    }
}
