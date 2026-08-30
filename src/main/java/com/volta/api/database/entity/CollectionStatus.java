package com.volta.api.database.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "collection_status")
public class CollectionStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "collection_id")
    private Collection collection;

    private String status;

    @Column(name = "changed_at")
    private LocalDateTime changedAt;

    private String observation;
}
