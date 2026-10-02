package com.volta.api.controller;

import com.volta.api.dto.request.WasteTypeRequestDTO;
import com.volta.api.dto.response.WasteTypeResponseDTO;
import com.volta.api.service.WasteTypeService;
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
@RequestMapping("/waste-types")
public class WasteTypeController {

    private final WasteTypeService wasteTypeService;

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<WasteTypeResponseDTO> create(@Valid @RequestBody WasteTypeRequestDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(wasteTypeService.register(dto));
    }

    @GetMapping
    public ResponseEntity<List<WasteTypeResponseDTO>> show(){
        List<WasteTypeResponseDTO> wasteTypes = wasteTypeService.getWasteTypes();
        return ResponseEntity.ok(wasteTypes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WasteTypeResponseDTO> show(@PathVariable UUID id){
        WasteTypeResponseDTO wasteType = wasteTypeService.getWasteTypeById(id);
        return ResponseEntity.ok(wasteType);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<WasteTypeResponseDTO> update(
            @PathVariable UUID id,
            @Valid @RequestBody WasteTypeRequestDTO dto
    ){
        WasteTypeResponseDTO response = wasteTypeService.update(id, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remove(@PathVariable UUID id){
        wasteTypeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
