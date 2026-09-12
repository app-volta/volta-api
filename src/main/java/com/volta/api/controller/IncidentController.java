package com.volta.api.controller;

import com.volta.api.dto.response.IncidentResponseDTO;
import com.volta.api.security.AuthenticatedUser;
import com.volta.api.usecase.RegisterIncidentUseCase;
import lombok.RequiredArgsConstructor;
import org.hibernate.query.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.awt.print.Pageable;

@RestController()
@RequiredArgsConstructor
@RequestMapping("/incident")
public class IncidentController {
    private final RegisterIncidentUseCase registerIncident;

    @GetMapping("/incidents")
    public IncidentResponseDTO list(@AuthenticationPrincipal AuthenticatedUser me, Pageable pageable) {
        return registerIncident.execute(me.companyId(), pageable);
    }
}
