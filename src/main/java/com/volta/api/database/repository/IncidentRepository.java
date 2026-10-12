package com.volta.api.database.repository;

import com.volta.api.database.entity.Incident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IncidentRepository extends JpaRepository<Incident, UUID> {

    List<Incident> findByCompanyId(UUID id);

    Optional<Incident> findByIdAndCompanyId(UUID id, UUID companyId);

    @Query("""
            SELECT i FROM Incident i
            WHERE i.company.id = :companyId
              AND (:status IS NULL OR i.status = :status)
              AND (:priority IS NULL OR i.priority = :priority)
              AND (:areaId IS NULL OR i.area.id = :areaId)
            ORDER BY i.registeredAt DESC
            """)
    List<Incident> search(
            @Param("companyId") UUID companyId,
            @Param("status") String status,
            @Param("priority") String priority,
            @Param("areaId") UUID areaId
    );
}
