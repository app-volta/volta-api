package com.volta.api.controller;

import com.volta.api.controller.docs.SummaryControllerDocs;
import com.volta.api.dto.summary.SummaryResponseDTO;
import com.volta.api.security.AuthenticatedUser;
import com.volta.api.usecase.SummaryUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/summary")
public class SummaryController implements SummaryControllerDocs {

    private final SummaryUseCase summaryUseCase;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER', 'EMPLOYEE')")
    public SummaryResponseDTO showSummary(@AuthenticationPrincipal AuthenticatedUser author) {
        SummaryResponseDTO summary = summaryUseCase.getSummary(author);
        return summary;
    }
}
