package com.volta.api.controller;

import com.volta.api.dto.request.WasteTypeRequestDTO;
import com.volta.api.dto.response.WasteTypeResponseDTO;
import com.volta.api.service.WasteTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/waste-types")
public class WasteTypeController {

    private final WasteTypeService wasteTypeService;

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<WasteTypeResponseDTO> create(@RequestBody WasteTypeRequestDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(wasteTypeService.register(dto));
    }

    @GetMapping
    public ResponseEntity<List<WasteTypeResponseDTO>> show(){
        List<WasteTypeResponseDTO> wasteTypes = wasteTypeService.getWasteTypes();
        return ResponseEntity.ok(wasteTypes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WasteTypeResponseDTO> show(UUID id){
        WasteTypeResponseDTO wasteType = wasteTypeService.getWasteTypeById(id);
        return ResponseEntity.ok(wasteType);
    }
}
