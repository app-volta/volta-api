package com.volta.api.controller;

import com.volta.api.dto.request.CooperativeRequestDTO;
import com.volta.api.dto.response.CooperativeResponseDTO;
import com.volta.api.usecase.CooperativeUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cooperatives")
public class CooperativeController {

    private final CooperativeUseCase cooperativeUseCase;

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<CooperativeResponseDTO> create(@Valid @RequestBody CooperativeRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cooperativeUseCase.register(dto));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<CooperativeResponseDTO>> show() {
        List<CooperativeResponseDTO> cooperatives = cooperativeUseCase.getCooperatives();
        return ResponseEntity.ok(cooperatives);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<CooperativeResponseDTO> show(@PathVariable UUID id) {
        CooperativeResponseDTO cooperative = cooperativeUseCase.getCooperativeById(id);
        return ResponseEntity.ok(cooperative);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<CooperativeResponseDTO> update(
            @PathVariable UUID id,
            @Valid @RequestBody CooperativeRequestDTO dto
    ) {
        CooperativeResponseDTO response = cooperativeUseCase.update(id, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> remove(@PathVariable UUID id) {
        cooperativeUseCase.delete(id);
        return ResponseEntity.noContent().build();
    }

}