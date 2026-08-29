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
public class Cooperative {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;
    private String cnpj;
    private double latitude;
    private double longitude;

    @Column(name = "average_rating")
    private double averageRating;

    private String specialties;

    @OneToMany(mappedBy = "cooperative")
    private Set<Collection> collections = new HashSet<>();

    @OneToMany(mappedBy = "cooperative")
    private Set<Conversation> conversations = new HashSet<>();

    @OneToMany(mappedBy = "cooperative")
    private Set<Review> reviews = new HashSet<>();

}
