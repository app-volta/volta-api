package com.volta.api.database.entity;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class WasteType {
    private Long id;
    private String category;
    private String description;
    private String defaultRiskLevel;
}
