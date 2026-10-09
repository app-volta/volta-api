package com.volta.api.service;

import com.volta.api.database.entity.Collection;
import com.volta.api.database.entity.Cooperative;
import com.volta.api.database.entity.Review;
import com.volta.api.database.entity.Users;
import com.volta.api.database.repository.CollectionRepository;
import com.volta.api.database.repository.CooperativeRepository;
import com.volta.api.database.repository.ReviewRepository;
import com.volta.api.database.repository.UserRepository;
import com.volta.api.dto.request.ReviewRequestDTO;
import com.volta.api.dto.response.ReviewResponseDTO;
import com.volta.api.enums.CollectionStatusType;
import com.volta.api.exception.BusinessRuleException;
import com.volta.api.exception.ConflictException;
import com.volta.api.exception.ResourceNotFoundException;
import com.volta.api.mapper.ReviewMapper;
import com.volta.api.security.AuthenticatedUser;
import com.volta.api.usecase.ReviewUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReviewService implements ReviewUseCase {

    private final ReviewRepository reviewRepository;
    private final CollectionRepository collectionRepository;
    private final CooperativeRepository cooperativeRepository;
    private final UserRepository userRepository;
    private final ReviewMapper reviewMapper;

    @Transactional
    public ReviewResponseDTO register(UUID collectionId, ReviewRequestDTO dto, AuthenticatedUser author) {
        Collection collection = collectionRepository.findByIdAndIncidentCompanyId(collectionId, author.companyId())
                .orElseThrow(() -> new ResourceNotFoundException("Collection"));

        if (!CollectionStatusType.COMPLETED.name().equals(collection.getCurrentStatus())) {
            throw new BusinessRuleException("Only completed collections can be reviewed");
        }

        if (reviewRepository.existsByCollectionId(collectionId)) {
            throw new ConflictException("Collection already reviewed");
        }

        Users user = userRepository.findById(author.id())
                .orElseThrow(() -> new ResourceNotFoundException("User"));

        Review review = reviewMapper.toEntity(dto, collection, user);
        Review saved = reviewRepository.saveAndFlush(review);

        updateAverageRating(collection.getCooperative());

        return reviewMapper.toResponse(saved);
    }

    public List<ReviewResponseDTO> getReviewsByCooperative(UUID cooperativeId) {
        if (!cooperativeRepository.existsById(cooperativeId)) {
            throw new ResourceNotFoundException("Cooperative");
        }

        List<ReviewResponseDTO> reviews = new ArrayList<>();

        for (Review review : reviewRepository.findByCooperativeIdOrderByReviewedAtDesc(cooperativeId)) {
            reviews.add(reviewMapper.toResponse(review));
        }

        return reviews;
    }

    private void updateAverageRating(Cooperative cooperative) {
        Double average = reviewRepository.findAverageStarsByCooperativeId(cooperative.getId());
        BigDecimal averageRating = average == null
                ? BigDecimal.ZERO
                : BigDecimal.valueOf(average).setScale(2, RoundingMode.HALF_UP);

        cooperative.setAverageRating(averageRating);
        cooperativeRepository.save(cooperative);
    }
}
