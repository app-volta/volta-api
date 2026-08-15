package com.volta.api.database.entity;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Cooperative {
    private Long id;
    private String name;
    private String cnpj;
    private double latitude;
    private double longitude;
    private double averageRating;
    private String specialties;
}
