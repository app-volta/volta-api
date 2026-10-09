package com.volta.api.controller;

import com.volta.api.controller.docs.WasteTypeControllerDocs;
import com.volta.api.dto.request.WasteTypeRequestDTO;
import com.volta.api.dto.response.WasteTypeResponseDTO;
import com.volta.api.usecase.WasteTypeUseCase;
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
public class WasteTypeController implements WasteTypeControllerDocs {

    private final WasteTypeUseCase wasteTypeUseCase;

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<WasteTypeResponseDTO> create(@Valid @RequestBody WasteTypeRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(wasteTypeUseCase.register(dto));
    }

    @GetMapping
    public ResponseEntity<List<WasteTypeResponseDTO>> show() {
        List<WasteTypeResponseDTO> wasteTypes = wasteTypeUseCase.getWasteTypes();
        return ResponseEntity.ok(wasteTypes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WasteTypeResponseDTO> show(@PathVariable UUID id) {
        WasteTypeResponseDTO wasteType = wasteTypeUseCase.getWasteTypeById(id);
        return ResponseEntity.ok(wasteType);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<WasteTypeResponseDTO> update(
            @PathVariable UUID id,
            @Valid @RequestBody WasteTypeRequestDTO dto
    ) {
        WasteTypeResponseDTO response = wasteTypeUseCase.update(id, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> remove(@PathVariable UUID id) {
        wasteTypeUseCase.delete(id);
        return ResponseEntity.noContent().build();
    }
}
