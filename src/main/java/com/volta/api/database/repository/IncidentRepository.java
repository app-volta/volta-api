package com.volta.api.database.repository;

import com.volta.api.database.entity.Incident;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IncidentRepository extends JpaRepository<Incident, UUID> {

    List<Incident> findByCompanyId(UUID id);

    Optional<Incident> findByIdAndCompanyId(UUID id, UUID companyId);
}
