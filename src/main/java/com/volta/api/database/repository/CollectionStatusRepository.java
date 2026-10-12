package com.volta.api.database.repository;

import com.volta.api.database.entity.CollectionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CollectionStatusRepository extends JpaRepository<CollectionStatus, UUID> {

    List<CollectionStatus> findByCollectionIdOrderByChangedAtAsc(UUID collectionId);

}
