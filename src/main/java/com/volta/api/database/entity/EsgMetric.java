package com.volta.api.database.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@ToString
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

    @Column(name = "total_waste_kg")
    private double totalWasteKg;

    @Column(name = "total_recycle_kg")
    private double totalRecycledKg;

    @Column(name = "recycling_percentage")
    private double recyclingPercentage;

    @Column(name = "calculated_at")
    private LocalDate calculatedAt;
}
