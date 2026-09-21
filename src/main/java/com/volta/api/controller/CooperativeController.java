package com.volta.api.controller;

import com.volta.api.dto.request.CooperativeRequestDTO;
import com.volta.api.dto.response.CooperativeResponseDTO;
import com.volta.api.service.CooperativeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cooperatives")
public class CooperativeController {

    private final CooperativeService cooperativeService;

    @PostMapping
    public ResponseEntity<CooperativeResponseDTO> create(@RequestBody CooperativeRequestDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(cooperativeService.register(dto));
    }

    @GetMapping
    public ResponseEntity<List<CooperativeResponseDTO>> show(){
        List<CooperativeResponseDTO> cooperatives = cooperativeService.getCooperatives();
        return ResponseEntity.ok(cooperatives);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CooperativeResponseDTO> show(@PathVariable UUID id){
        CooperativeResponseDTO cooperative = cooperativeService.getCooperativeById(id);
        return ResponseEntity.ok(cooperative);
    }
}
