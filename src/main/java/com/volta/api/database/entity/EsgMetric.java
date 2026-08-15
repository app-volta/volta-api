package com.volta.api.database.entity;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EsgMetric {
    private Long id;
    private Company company;
    private String period;
    private double totalWasteKg;
    private double totaRecycledKg;
    private double recyclingPercentage;
    private LocalDate calculatedAt;
}
