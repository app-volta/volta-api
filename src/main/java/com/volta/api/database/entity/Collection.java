package com.volta.api.database.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "collection")
public class Collection {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "incident_id")
    private Incident incident;

    @ManyToOne
    @JoinColumn(name = "cooperative_id")
    private Cooperative cooperative;

    @Column(name = "request_date")
    private LocalDate requestDate;

    @Column(name = "scheduled_date")
    private LocalDate scheduledDate;

    @Column(name = "current_status")
    private String currentStatus;

    @Column(name = "collection_type")
    private String collectionType;

    private boolean urgent;

    @OneToMany(mappedBy = "collection")
    private Set<CollectionStatus> collectionStatuses = new HashSet<>();

    @OneToMany(mappedBy = "collection")
    private Set<Conversation> conversations = new HashSet<>();

    @OneToMany(mappedBy = "collection")
    private Set<Review> reviews = new HashSet<>();
}