package com.volta.api.database.entity;

import jakarta.persistence.*;
import lombok.*;

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
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    private String period;

    @Column(name = "total_waste_kg", columnDefinition = "DECIMAL(14,2)")
    private BigDecimal totalWasteKg;

    @Column(name = "total_recycled_kg", columnDefinition = "DECIMAL(14,2)")
    private BigDecimal totalRecycledKg;

    @Column(name = "recycling_percentage", columnDefinition = "DECIMAL(5,2)")
    private BigDecimal recyclingPercentage;

    @Column(name = "calculated_at")
    private LocalDateTime calculatedAt;
}
