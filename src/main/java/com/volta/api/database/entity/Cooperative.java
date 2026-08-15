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
@ToString
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

    @OneToMany(mappedBy = "Collection")
    private Set<Collection> collections = new HashSet<>();

    @OneToMany(mappedBy = "Conversation")
    private Set<Conversation> conversations = new HashSet<>();

    @OneToMany(mappedBy = "Review")
    private Set<Review> reviews = new HashSet<>();

}
