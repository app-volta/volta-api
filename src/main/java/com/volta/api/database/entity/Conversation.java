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
public class Conversation {
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
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "cooperative_id", nullable = false)
    private Cooperative cooperative;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "collection_id", nullable = false)
    private Collection collection;

    @NotNull
    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "conversation")
    private Set<Message> messages = new HashSet<>();
}
