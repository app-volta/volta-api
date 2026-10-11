package com.volta.api.controller;

import com.volta.api.controller.docs.ReviewControllerDocs;
import com.volta.api.dto.review.ReviewRequestDTO;
import com.volta.api.dto.review.ReviewResponseDTO;
import com.volta.api.security.AuthenticatedUser;
import com.volta.api.usecase.ReviewUseCase;
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
public class ReviewController implements ReviewControllerDocs {

    private final ReviewUseCase reviewUseCase;

    @PostMapping("/collections/{collectionId}/review")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    @ResponseStatus(HttpStatus.CREATED)
    public ReviewResponseDTO create(
            @PathVariable UUID collectionId,
            @Valid @RequestBody ReviewRequestDTO dto,
            @AuthenticationPrincipal AuthenticatedUser author
    ) {
        return reviewUseCase.register(collectionId, dto, author);
    }

    @GetMapping("/cooperatives/{cooperativeId}/reviews")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    public List<ReviewResponseDTO> showByCooperative(@PathVariable UUID cooperativeId) {
        List<ReviewResponseDTO> reviews = reviewUseCase.getReviewsByCooperative(cooperativeId);
        return reviews;
    }
}
