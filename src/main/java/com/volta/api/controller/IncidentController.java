package com.volta.api.controller;

import com.volta.api.dto.request.RegisterIncidentRequestDTO;
import com.volta.api.dto.response.IncidentResponseDTO;
import com.volta.api.security.AuthenticatedUser;
import com.volta.api.service.RegisterIncidentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/incidents")
public class IncidentController {

    private final RegisterIncidentService registerIncidentService;

    @PostMapping
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public ResponseEntity<IncidentResponseDTO> create(@RequestBody RegisterIncidentRequestDTO dto, AuthenticatedUser author){
        return ResponseEntity.status(HttpStatus.CREATED).body(registerIncidentService.register(dto, author));
    }
}
