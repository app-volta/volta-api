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
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    private String cnpj;

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