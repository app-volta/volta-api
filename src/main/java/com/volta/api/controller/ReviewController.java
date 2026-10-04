package com.volta.api.controller;

import com.volta.api.dto.request.ReviewRequestDTO;
import com.volta.api.dto.response.ReviewResponseDTO;
import com.volta.api.security.AuthenticatedUser;
import com.volta.api.usecase.ReviewUseCase;
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
public class ReviewController {

    private final ReviewUseCase reviewUseCase;

    @PostMapping("/collections/{collectionId}/review")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    public ResponseEntity<ReviewResponseDTO> create(
            @PathVariable UUID collectionId,
            @Valid @RequestBody ReviewRequestDTO dto,
            @AuthenticationPrincipal AuthenticatedUser author
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewUseCase.register(collectionId, dto, author));
    }

    @GetMapping("/cooperatives/{cooperativeId}/reviews")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    public ResponseEntity<List<ReviewResponseDTO>> showByCooperative(@PathVariable UUID cooperativeId) {
        List<ReviewResponseDTO> reviews = reviewUseCase.getReviewsByCooperative(cooperativeId);
        return ResponseEntity.ok(reviews);
    }
}
