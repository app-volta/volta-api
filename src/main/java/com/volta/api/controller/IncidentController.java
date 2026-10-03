package com.volta.api.controller;

import com.volta.api.dto.request.IncidentRequestDTO;
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
public class IncidentController {

    private final IncidentUseCase incidentUseCase;

    @PostMapping
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public ResponseEntity<IncidentResponseDTO> create(
            @Valid @RequestBody IncidentRequestDTO dto,
            @AuthenticationPrincipal AuthenticatedUser author
    ){
        return ResponseEntity.status(HttpStatus.CREATED).body(incidentUseCase.register(dto, author));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public ResponseEntity<List<IncidentResponseDTO>> show(@AuthenticationPrincipal AuthenticatedUser author){
        List<IncidentResponseDTO> incidents = incidentUseCase.getIncidents(author);
        return ResponseEntity.ok(incidents);
    }

    @GetMapping("/{id}")
    public ResponseEntity<IncidentResponseDTO> show(
            @PathVariable UUID id,
            @AuthenticationPrincipal AuthenticatedUser author
    ){
        IncidentResponseDTO incident = incidentUseCase.getIncidentById(id, author);
        return ResponseEntity.ok(incident);
    }
}
