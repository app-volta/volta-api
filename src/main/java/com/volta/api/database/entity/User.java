package com.volta.api.database.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class User {
    private Long id;
    private Company company;
    private String varchar;
    private String email;
    @JsonIgnore
    private String passwordHash;
    private String role;
    private String userType;
}
