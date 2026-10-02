package com.volta.api.controller;

import com.volta.api.dto.request.AreaRequestDTO;
import com.volta.api.dto.response.AreaResponseDTO;
import com.volta.api.security.AuthenticatedUser;
import com.volta.api.service.AreaService;
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

    private final AreaService areaService;

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<AreaResponseDTO> create(@Valid @RequestBody AreaRequestDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(areaService.register(dto));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<AreaResponseDTO>> show(){
        List<AreaResponseDTO> areas = areaService.getAreas();
        return ResponseEntity.ok(areas);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<AreaResponseDTO> show(@PathVariable UUID id){
        AreaResponseDTO area = areaService.getAreaById(id);
        return ResponseEntity.ok(area);
    }

    @GetMapping("/mine")
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public ResponseEntity<List<AreaResponseDTO>> showMine(@AuthenticationPrincipal AuthenticatedUser author){
        List<AreaResponseDTO> area = areaService.getAreasOfMyCompany(author);
        return ResponseEntity.ok(area);
    }
}