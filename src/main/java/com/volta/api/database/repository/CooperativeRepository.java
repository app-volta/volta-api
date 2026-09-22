package com.volta.api.database.repository;

import com.volta.api.database.entity.Cooperative;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CooperativeRepository extends JpaRepository<Cooperative, UUID> {
}
