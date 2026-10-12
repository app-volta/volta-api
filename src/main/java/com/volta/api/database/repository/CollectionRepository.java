package com.volta.api.database.repository;

import com.volta.api.database.entity.Collection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CollectionRepository extends JpaRepository<Collection, UUID> {

    List<Collection> findByIncidentCompanyId(UUID companyId);

    Optional<Collection> findByIdAndIncidentCompanyId(UUID id, UUID companyId);

    boolean existsByIncidentIdAndCurrentStatusIn(UUID incidentId, List<String> statuses);

    boolean existsByIncidentIdAndCurrentStatus(UUID incidentId, String status);

}
