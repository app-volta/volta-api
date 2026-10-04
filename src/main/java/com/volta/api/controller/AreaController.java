package com.volta.api.controller;

import com.volta.api.dto.request.AreaRequestDTO;
import com.volta.api.dto.request.update.AreaUpdateRequestDTO;
import com.volta.api.dto.response.AreaResponseDTO;
import com.volta.api.security.AuthenticatedUser;
import com.volta.api.usecase.AreaUseCase;
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
@RequestMapping("/areas")
public class AreaController {

    private final AreaUseCase areaUseCase;

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<AreaResponseDTO> create(@Valid @RequestBody AreaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(areaUseCase.register(dto));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<AreaResponseDTO>> show() {
        List<AreaResponseDTO> areas = areaUseCase.getAreas();
        return ResponseEntity.ok(areas);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<AreaResponseDTO> show(@PathVariable UUID id) {
        AreaResponseDTO area = areaUseCase.getAreaById(id);
        return ResponseEntity.ok(area);
    }

    @GetMapping("/mine")
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public ResponseEntity<List<AreaResponseDTO>> showMine(@AuthenticationPrincipal AuthenticatedUser author) {
        List<AreaResponseDTO> area = areaUseCase.getAreasOfMyCompany(author);
        return ResponseEntity.ok(area);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<AreaResponseDTO> update(
            @PathVariable UUID id,
            @Valid @RequestBody AreaUpdateRequestDTO dto
    ) {
        AreaResponseDTO response = areaUseCase.update(id, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> remove(@PathVariable UUID id) {
        areaUseCase.delete(id);
        return ResponseEntity.noContent().build();
    }
}