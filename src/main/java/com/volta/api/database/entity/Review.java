package com.volta.api.database.entity;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Review {
    private Long id;
    private Cooperative cooperative;
    private User user;
    private Collection collection;
    private int stars;
    private String comment;
    private LocalDate reviewDate;
}
