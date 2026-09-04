package com.volta.api.database.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
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
    @Column(
            columnDefinition = "UUID DEFAULT gen_random_uuid()",
            updatable = false,
            nullable = false,
            unique = true
    )
    private UUID id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "incident_id", nullable = false)
    private Incident incident;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "cooperative_id", nullable = false)
    private Cooperative cooperative;
    @NotNull
    @CreationTimestamp
    @Column(name = "requested_at")
    private LocalDateTime requestedAt;

    @Column(name = "scheduled_at")
    private LocalDateTime scheduledAt;

    @NotNull
    @Column(name = "current_status", length = 50, nullable = false)
    private String currentStatus;

    @NotNull
    @Column(name = "collection_type", length = 50, nullable = false)
    private String collectionType;

    @NotNull
    @Column(nullable = false)
    private boolean urgent = false;

    @OneToMany(mappedBy = "collection")
    private Set<CollectionStatus> collectionStatuses = new HashSet<>();

    @OneToMany(mappedBy = "collection")
    private Set<Conversation> conversations = new HashSet<>();

    @OneToMany(mappedBy = "collection")
    private Set<Review> reviews = new HashSet<>();
}