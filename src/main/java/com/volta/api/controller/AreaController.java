package com.volta.api.controller;

import com.volta.api.controller.docs.AreaControllerDocs;
import com.volta.api.dto.area.AreaRequestDTO;
import com.volta.api.dto.area.AreaUpdateRequestDTO;
import com.volta.api.dto.area.AreaResponseDTO;
import com.volta.api.security.AuthenticatedUser;
import com.volta.api.usecase.AreaUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/areas")
public class AreaController implements AreaControllerDocs {

    private final AreaUseCase areaUseCase;

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public AreaResponseDTO create(@Valid @RequestBody AreaRequestDTO dto) {
        return areaUseCase.register(dto);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<AreaResponseDTO> show() {
        List<AreaResponseDTO> areas = areaUseCase.getAreas();
        return areas;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public AreaResponseDTO show(@PathVariable UUID id) {
        AreaResponseDTO area = areaUseCase.getAreaById(id);
        return area;
    }

    @GetMapping("/mine")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER', 'EMPLOYEE')")
    public List<AreaResponseDTO> showMine(@AuthenticationPrincipal AuthenticatedUser author) {
        List<AreaResponseDTO> area = areaUseCase.getAreasOfMyCompany(author);
        return area;
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public AreaResponseDTO update(
            @PathVariable UUID id,
            @Valid @RequestBody AreaUpdateRequestDTO dto
    ) {
        AreaResponseDTO response = areaUseCase.update(id, dto);
        return response;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(@PathVariable UUID id) {
        areaUseCase.delete(id);
    }
}