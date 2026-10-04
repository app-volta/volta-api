package com.volta.api.database.function;

import com.volta.api.database.entity.Collection;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.UUID;

public interface CollectionFunction extends Repository<Collection, UUID> {

    @Query(value = "SELECT calculate_collection_completion_hours(:p_collection_id)", nativeQuery = true)
    BigDecimal calculateCollectionCompletionHours(@Param("p_collection_id") UUID collectionId);
}
