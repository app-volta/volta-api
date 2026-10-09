package com.volta.api.database.repository;

import com.volta.api.database.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, UUID> {

    List<Review> findByCooperativeIdOrderByReviewedAtDesc(UUID cooperativeId);

    boolean existsByCollectionId(UUID collectionId);

    @Query("SELECT AVG(r.stars) FROM Review r WHERE r.cooperative.id = :cooperativeId")
    Double findAverageStarsByCooperativeId(@Param("cooperativeId") UUID cooperativeId);
}
