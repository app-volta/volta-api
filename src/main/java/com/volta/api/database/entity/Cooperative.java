package com.volta.api.database.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
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

    @Column(columnDefinition = "DECIMAL(9,6)")
    private BigDecimal latitude;

    @Column(columnDefinition = "DECIMAL(9,6)")
    private BigDecimal longitude;

    @Column(name = "average_rating", columnDefinition = "NUMERIC(3,2)")
    private BigDecimal averageRating;

    private String specialties;

    @OneToMany(mappedBy = "cooperative")
    private Set<Collection> collections = new HashSet<>();

    @OneToMany(mappedBy = "cooperative")
    private Set<Conversation> conversations = new HashSet<>();

    @OneToMany(mappedBy = "cooperative")
    private Set<Review> reviews = new HashSet<>();

}
