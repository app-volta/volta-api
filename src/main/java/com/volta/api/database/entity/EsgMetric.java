package com.volta.api.database.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "esg_metric")
public class EsgMetric {
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
    @Column(length = 20, nullable = false)
    private String period;

    @NotNull
    @Column(name = "total_waste_kg", columnDefinition = "DECIMAL(14,2)", nullable = false)
    private BigDecimal totalWasteKg = BigDecimal.valueOf(0);

    @NotNull
    @Column(name = "total_recycled_kg", columnDefinition = "DECIMAL(14,2)", nullable = false)
    private BigDecimal totalRecycledKg = BigDecimal.valueOf(0);

    @NotNull
    @Column(name = "recycling_percentage", columnDefinition = "DECIMAL(5,2)", nullable = false)
    private BigDecimal recyclingPercentage = BigDecimal.valueOf(0);

    @NotNull
    @CreationTimestamp
    @Column(name = "calculated_at", nullable = false)
    private LocalDateTime calculatedAt;
}
