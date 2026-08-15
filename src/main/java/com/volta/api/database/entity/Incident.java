package com.volta.api.database.entity;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Incident {
    private Long id;
    private Company company;
    private User user;
    private Area area;
    private WasteType wasteType;
    private String photoUrl;
    private String employeeDescription;
    private String contaminationLevel;
    private String estimatedQuantity;
    private String priority;
    private String status;
    private LocalDate registeredAt;
}
