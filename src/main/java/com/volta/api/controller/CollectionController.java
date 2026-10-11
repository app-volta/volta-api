package com.volta.api.controller;

import com.volta.api.controller.docs.CollectionControllerDocs;
import com.volta.api.dto.collection.CollectionRequestDTO;
import com.volta.api.dto.collection.CollectionScheduleRequestDTO;
import com.volta.api.dto.collection.CollectionStatusRequestDTO;
import com.volta.api.dto.collection.CollectionCompletionTimeResponseDTO;
import com.volta.api.dto.collection.CollectionResponseDTO;
import com.volta.api.dto.collection.CollectionStatusResponseDTO;
import com.volta.api.security.AuthenticatedUser;
import com.volta.api.usecase.CollectionUseCase;
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
@RequestMapping("/collections")
public class CollectionController implements CollectionControllerDocs {

    private final CollectionUseCase collectionUseCase;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    @ResponseStatus(HttpStatus.CREATED)
    public CollectionResponseDTO create(
            @Valid @RequestBody CollectionRequestDTO dto,
            @AuthenticationPrincipal AuthenticatedUser author
    ) {
        return collectionUseCase.register(dto, author);
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    public List<CollectionResponseDTO> show(@AuthenticationPrincipal AuthenticatedUser author) {
        List<CollectionResponseDTO> collections = collectionUseCase.getCollections(author);
        return collections;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    public CollectionResponseDTO showById(
            @PathVariable UUID id,
            @AuthenticationPrincipal AuthenticatedUser author
    ) {
        CollectionResponseDTO collection = collectionUseCase.getCollectionById(id, author);
        return collection;
    }

    @GetMapping("/{id}/completion-time")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    public CollectionCompletionTimeResponseDTO showCompletionTime(
            @PathVariable UUID id,
            @AuthenticationPrincipal AuthenticatedUser author
    ) {
        CollectionCompletionTimeResponseDTO completionTime = collectionUseCase.getCompletionTime(id, author);
        return completionTime;
    }

    @PatchMapping("/{id}/schedule")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void schedule(
            @PathVariable UUID id,
            @Valid @RequestBody CollectionScheduleRequestDTO dto,
            @AuthenticationPrincipal AuthenticatedUser author
    ) {
        collectionUseCase.schedule(id, dto, author);
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateStatus(
            @PathVariable UUID id,
            @Valid @RequestBody CollectionStatusRequestDTO dto,
            @AuthenticationPrincipal AuthenticatedUser author
    ) {
        collectionUseCase.updateStatus(id, dto, author);
    }

    @GetMapping("/{id}/history")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    public List<CollectionStatusResponseDTO> show(
            @PathVariable UUID id,
            @AuthenticationPrincipal AuthenticatedUser author
    ) {
        List<CollectionStatusResponseDTO> collectionStatuses = collectionUseCase.getCollectionStatus(id, author);
        return collectionStatuses;
    }
}