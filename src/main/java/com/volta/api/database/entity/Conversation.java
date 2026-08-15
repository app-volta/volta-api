package com.volta.api.database.entity;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Conversation {
    private Long id;
    private Company company;
    private Cooperative cooperative;
    private Collection collection;
    private LocalDate createdAt;
}
