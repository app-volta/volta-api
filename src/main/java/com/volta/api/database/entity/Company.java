package com.volta.api.database.entity;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Company {
    private Long id;
    private String name;
    private String cnpj;
    private String address;
}