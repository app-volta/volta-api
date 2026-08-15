package com.volta.api.database.entity;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Collection {
    private Long id;
    private Incident incident;
    private Cooperative cooperative;
    private LocalDate requestDate;
    private LocalDate scheduledDate;
    private String currentStatus;
    private String collectionType;
    private boolean urgent;
}
