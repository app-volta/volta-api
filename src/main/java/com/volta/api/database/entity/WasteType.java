package com.volta.api.database.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "waste_type")
public class WasteType {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String category;

    private String description;

    @Column(name = "default_risk_level")
    private String defaultRiskLevel;

    @OneToMany(mappedBy = "wasteType")
    private Set<Incident> incidents = new HashSet<>();
}
