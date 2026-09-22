package com.volta.api.controller;

import com.volta.api.dto.request.AreaRequestDTO;
import com.volta.api.dto.response.AreaResponseDTO;
import com.volta.api.security.AuthenticatedUser;
import com.volta.api.service.AreaService;
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
    public ResponseEntity<AreaResponseDTO> create(
            @RequestBody AreaRequestDTO dto,
            @AuthenticationPrincipal AuthenticatedUser author
    ){
        return ResponseEntity.status(HttpStatus.CREATED).body(areaService.register(dto, author));
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
}