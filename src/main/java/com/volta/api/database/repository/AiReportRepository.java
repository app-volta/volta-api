package com.volta.api.database.repository;

import com.volta.api.database.entity.AiReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AiReportRepository extends JpaRepository<AiReport, UUID> {

    Optional<AiReport> findFirstByIncidentIdOrderByGeneratedAtDesc(UUID incidentId);
}
