package com.volta.api.controller;

import com.volta.api.controller.docs.CooperativeControllerDocs;
import com.volta.api.dto.cooperative.CooperativeRequestDTO;
import com.volta.api.dto.cooperative.CooperativeResponseDTO;
import com.volta.api.dto.scoring.CooperativeScoringDataDTO;
import com.volta.api.usecase.CooperativeUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cooperatives")
public class CooperativeController implements CooperativeControllerDocs {

    private final CooperativeUseCase cooperativeUseCase;

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public CooperativeResponseDTO create(@Valid @RequestBody CooperativeRequestDTO dto) {
        return cooperativeUseCase.register(dto);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<CooperativeResponseDTO> show() {
        List<CooperativeResponseDTO> cooperatives = cooperativeUseCase.getCooperatives();
        return cooperatives;
    }

    @GetMapping("/scoring-data")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER', 'EMPLOYEE')")
    public List<CooperativeScoringDataDTO> showScoringData() {
        List<CooperativeScoringDataDTO> cooperatives = cooperativeUseCase.getScoringData();
        return cooperatives;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public CooperativeResponseDTO show(@PathVariable UUID id) {
        CooperativeResponseDTO cooperative = cooperativeUseCase.getCooperativeById(id);
        return cooperative;
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public CooperativeResponseDTO update(
            @PathVariable UUID id,
            @Valid @RequestBody CooperativeRequestDTO dto
    ) {
        CooperativeResponseDTO response = cooperativeUseCase.update(id, dto);
        return response;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(@PathVariable UUID id) {
        cooperativeUseCase.delete(id);
    }

}