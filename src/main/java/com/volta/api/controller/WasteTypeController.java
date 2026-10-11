package com.volta.api.controller;

import com.volta.api.controller.docs.WasteTypeControllerDocs;
import com.volta.api.dto.wastetype.WasteTypeRequestDTO;
import com.volta.api.dto.wastetype.WasteTypeResponseDTO;
import com.volta.api.usecase.WasteTypeUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    @ResponseStatus(HttpStatus.CREATED)
    public WasteTypeResponseDTO create(@Valid @RequestBody WasteTypeRequestDTO dto) {
        return wasteTypeUseCase.register(dto);
    }

    @GetMapping
    public List<WasteTypeResponseDTO> show() {
        List<WasteTypeResponseDTO> wasteTypes = wasteTypeUseCase.getWasteTypes();
        return wasteTypes;
    }

    @GetMapping("/{id}")
    public WasteTypeResponseDTO show(@PathVariable UUID id) {
        WasteTypeResponseDTO wasteType = wasteTypeUseCase.getWasteTypeById(id);
        return wasteType;
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public WasteTypeResponseDTO update(
            @PathVariable UUID id,
            @Valid @RequestBody WasteTypeRequestDTO dto
    ) {
        WasteTypeResponseDTO response = wasteTypeUseCase.update(id, dto);
        return response;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(@PathVariable UUID id) {
        wasteTypeUseCase.delete(id);
    }
}
