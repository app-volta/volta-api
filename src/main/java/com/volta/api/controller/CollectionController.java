package com.volta.api.controller;

import com.volta.api.dto.request.CollectionRequestDTO;
import com.volta.api.dto.request.CollectionScheduleRequestDTO;
import com.volta.api.dto.request.CollectionStatusRequestDTO;
import com.volta.api.dto.response.CollectionCompletionTimeResponseDTO;
import com.volta.api.dto.response.CollectionResponseDTO;
import com.volta.api.dto.response.CollectionStatusResponseDTO;
import com.volta.api.security.AuthenticatedUser;
import com.volta.api.usecase.CollectionUseCase;
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
@RequestMapping("/collections")
public class CollectionController {

    private final CollectionUseCase collectionUseCase;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    public ResponseEntity<CollectionResponseDTO> create(
            @Valid @RequestBody CollectionRequestDTO dto,
            @AuthenticationPrincipal AuthenticatedUser author
    ){
        return ResponseEntity.status(HttpStatus.CREATED).body(collectionUseCase.register(dto, author));
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    public ResponseEntity<List<CollectionResponseDTO>> show(@AuthenticationPrincipal AuthenticatedUser author){
        List<CollectionResponseDTO> collections = collectionUseCase.getCollections(author);
        return ResponseEntity.ok(collections);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    public ResponseEntity<CollectionResponseDTO> showById(
            @PathVariable UUID id,
            @AuthenticationPrincipal AuthenticatedUser author
    ){
        CollectionResponseDTO collection = collectionUseCase.getCollectionById(id, author);
        return ResponseEntity.ok(collection);
    }

    @GetMapping("/{id}/completion-time")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    public ResponseEntity<CollectionCompletionTimeResponseDTO> showCompletionTime(
            @PathVariable UUID id,
            @AuthenticationPrincipal AuthenticatedUser author
    ){
        CollectionCompletionTimeResponseDTO completionTime = collectionUseCase.getCompletionTime(id, author);
        return ResponseEntity.ok(completionTime);
    }

    @PatchMapping("/{id}/schedule")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    public ResponseEntity<Void> schedule(
            @PathVariable UUID id,
            @Valid @RequestBody CollectionScheduleRequestDTO dto,
            @AuthenticationPrincipal AuthenticatedUser author
    ) {
        collectionUseCase.schedule(id, dto, author);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    public ResponseEntity<Void> updateStatus(
            @PathVariable UUID id,
            @Valid @RequestBody CollectionStatusRequestDTO dto,
            @AuthenticationPrincipal AuthenticatedUser author
    ) {
        collectionUseCase.updateStatus(id, dto, author);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/history")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    public ResponseEntity<List<CollectionStatusResponseDTO>> show(
            @PathVariable UUID id,
            @AuthenticationPrincipal AuthenticatedUser author
    ){
        List<CollectionStatusResponseDTO> collectionStatuses = collectionUseCase.getCollectionStatus(id, author);
        return ResponseEntity.ok(collectionStatuses);
    }
}