package com.volta.api.database.function;

import com.volta.api.database.entity.Collection;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.UUID;

public interface CollectionFunction extends Repository<Collection, UUID> {

    @NativeQuery("SELECT calculate_collection_completion_hours(:p_collection_id)")
    BigDecimal calculateCollectionCompletionHours(@Param("p_collection_id") UUID collectionId);
}
