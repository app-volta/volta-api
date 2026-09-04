package com.volta.api.database.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
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
public class Incident {
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
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "area_id", nullable = false)
    private Area area;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "waste_type_id", nullable = false)
    private WasteType wasteType;

    @Column(name = "photo_url", length = 500)
    private String photoUrl;

    @NotNull
    @Column(name = "employee_description", nullable = false)
    private String employeeDescription;

    @Column(name = "contamination_level", length = 50)
    private String contaminationLevel;

    @Column(name = "estimated_quantity", columnDefinition = "DECIMAL(12,2)")
    private BigDecimal estimatedQuantity;

    @NotNull
    @Column(length = 30, nullable = false)
    private String priority;

    @NotNull
    @Column(length = 50, nullable = false)
    private String status;

    @NotNull
    @Column(
            name = "registered_at",
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP",
            nullable = false
    )
    private LocalDateTime registeredAt;

    @OneToMany(mappedBy = "incident")
    private Set<Attachment> attachments = new HashSet<>();

    @OneToMany(mappedBy = "incident")
    private Set<Collection> collections = new HashSet<>();
}
