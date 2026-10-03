package com.volta.api.database.repository;

import com.volta.api.database.entity.Area;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AreaRepository extends JpaRepository<Area, UUID> {

    Optional<Area> findByIdAndCompanyId(UUID id, UUID companyId);

    List<Area> findByCompanyId(UUID companyId);

}