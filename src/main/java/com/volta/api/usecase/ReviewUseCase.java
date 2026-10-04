package com.volta.api.usecase;

import com.volta.api.dto.request.ReviewRequestDTO;
import com.volta.api.dto.response.ReviewResponseDTO;
import com.volta.api.security.AuthenticatedUser;

import java.util.List;
import java.util.UUID;

public interface ReviewUseCase {

    ReviewResponseDTO register(UUID collectionId, ReviewRequestDTO dto, AuthenticatedUser author);

    List<ReviewResponseDTO> getReviewsByCooperative(UUID cooperativeId);
}
