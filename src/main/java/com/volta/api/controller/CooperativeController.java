package com.volta.api.controller;

import com.volta.api.dto.request.CooperativeRequestDTO;
import com.volta.api.dto.response.CooperativeResponseDTO;
import com.volta.api.service.CooperativeService;
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

    private final CooperativeService cooperativeService;

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<CooperativeResponseDTO> create(@Valid @RequestBody CooperativeRequestDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(cooperativeService.register(dto));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<CooperativeResponseDTO>> show(){
        List<CooperativeResponseDTO> cooperatives = cooperativeService.getCooperatives();
        return ResponseEntity.ok(cooperatives);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<CooperativeResponseDTO> show(@PathVariable UUID id){
        CooperativeResponseDTO cooperative = cooperativeService.getCooperativeById(id);
        return ResponseEntity.ok(cooperative);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<CooperativeResponseDTO> update(
            @PathVariable UUID id,
            @Valid @RequestBody CooperativeRequestDTO dto
    ){
        CooperativeResponseDTO response = cooperativeService.update(id, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> remove(@PathVariable UUID id){
        cooperativeService.delete(id);
        return ResponseEntity.noContent().build();
    }

}