package com.volta.api.database.procedure;

import com.volta.api.database.entity.Collection;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

public interface CollectionProcedure extends Repository<Collection, UUID> {

    @Transactional
    @Procedure(procedureName = "update_collection_status")
    void updateCollectionStatus(
            @Param("p_collection_id") UUID collectionId,
            @Param("p_new_status") String newStatus,
            @Param("p_observation") String observation
    );

    @Transactional
    @Procedure(procedureName = "schedule_collection")
    void scheduleCollection(
            @Param("p_collection_id") UUID collectionId,
            @Param("p_scheduled_at") LocalDateTime scheduledAt
    );
}
