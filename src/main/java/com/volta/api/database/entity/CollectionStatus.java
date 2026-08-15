package com.volta.api.database.entity;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class CollectionStatus {
    private Long id;
    private Collection collection;
    private String status;
    private LocalDate changeDate;
    private String observation;
}
