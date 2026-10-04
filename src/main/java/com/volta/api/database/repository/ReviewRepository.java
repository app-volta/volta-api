package com.volta.api.database.repository;

import com.volta.api.database.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, UUID> {

    List<Review> findByCooperativeIdOrderByReviewedAtDesc(UUID cooperativeId);

    boolean existsByCollectionId(UUID collectionId);
}
