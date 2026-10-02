package com.volta.api.controller;

import com.volta.api.dto.request.CompanyRequestDTO;
import com.volta.api.dto.response.CompanyResponseDTO;
import com.volta.api.service.CompanyService;
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
@RequestMapping("/companies")
public class CompanyController {

    private final CompanyService companyService;

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<CompanyResponseDTO> create(@Valid @RequestBody CompanyRequestDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(companyService.register(dto));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<CompanyResponseDTO>> show(){
        List<CompanyResponseDTO> companies = companyService.getCompanies();
        return ResponseEntity.ok(companies);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<CompanyResponseDTO> show(@PathVariable UUID id){
        CompanyResponseDTO company = companyService.getCompanyById(id);
        return ResponseEntity.ok(company);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<CompanyResponseDTO> update(
            @PathVariable UUID id,
            @Valid @RequestBody CompanyRequestDTO dto
    ){
        CompanyResponseDTO response = companyService.update(id, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> remove(@PathVariable UUID id){
        companyService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
