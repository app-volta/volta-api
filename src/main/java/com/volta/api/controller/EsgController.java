package com.volta.api.controller;

import com.volta.api.dto.request.RecyclingPercentageRequestDTO;
import com.volta.api.dto.response.EsgScoreResponseDTO;
import com.volta.api.dto.response.RecyclingPercentageResponseDTO;
import com.volta.api.security.AuthenticatedUser;
import com.volta.api.usecase.EsgUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/esg")
public class EsgController {

    private final EsgUseCase esgUseCase;

    @PostMapping("/recycling-percentage")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    public ResponseEntity<RecyclingPercentageResponseDTO> calculateRecyclingPercentage(
            @Valid @RequestBody RecyclingPercentageRequestDTO dto
    ){
        return ResponseEntity.ok(esgUseCase.calculateRecyclingPercentage(dto));
    }

    @GetMapping("/score")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    public ResponseEntity<EsgScoreResponseDTO> showScore(@AuthenticationPrincipal AuthenticatedUser author){
        EsgScoreResponseDTO score = esgUseCase.getCompanyScore(author);
        return ResponseEntity.ok(score);
    }
}
