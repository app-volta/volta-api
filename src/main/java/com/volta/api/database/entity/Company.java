package com.volta.api.database.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(
            columnDefinition = "UUID DEFAULT gen_random_uuid()",
            updatable = false,
            nullable = false,
            unique = true
    )
    private UUID id;

    @NotNull
    @Column(length = 150, nullable = false)
    private String name;

    @NotNull
    @Column(length = 18, nullable = false)
    private String cnpj;

    @NotNull
    @Column(nullable = false)
    private String address;

    @OneToMany(mappedBy = "company")
    private Set<Conversation> conversations = new HashSet<>();

    @OneToMany(mappedBy = "company")
    private Set<Area> areas = new HashSet<>();

    @OneToMany(mappedBy = "company")
    private Set<EsgMetric> esgMetrics = new HashSet<>();

    @OneToMany(mappedBy = "company")
    private Set<Incident> incidents = new HashSet<>();

}