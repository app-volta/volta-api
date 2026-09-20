package com.volta.api.database.repository;

import com.volta.api.database.entity.WasteType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface WasteTypeRepository extends JpaRepository<WasteType, UUID> {
}
