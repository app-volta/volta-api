package com.volta.api.database.entity;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Area {
    private Long id;
    private Company company;
    private String sectorName;
    private String locationDescription;
}
