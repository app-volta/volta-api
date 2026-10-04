package com.volta.api.mapper;

import com.volta.api.database.entity.Collection;
import com.volta.api.database.entity.Review;
import com.volta.api.database.entity.Users;
import com.volta.api.dto.request.ReviewRequestDTO;
import com.volta.api.dto.response.ReviewResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {

    public Review toEntity(ReviewRequestDTO dto, Collection collection, Users user) {
        Review review = new Review();

        review.setCollection(collection);
        review.setCooperative(collection.getCooperative());
        review.setUser(user);
        review.setStars(dto.stars());
        review.setComment(dto.comment());

        return review;
    }

    public ReviewResponseDTO toResponse(Review review) {
        return new ReviewResponseDTO(
                review.getId(),
                review.getCooperative().getId(),
                review.getUser().getId(),
                review.getCollection().getId(),
                review.getStars(),
                review.getComment(),
                review.getReviewedAt()
        );
    }
}
