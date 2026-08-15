package com.volta.api.database.entity;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Notification {
    private Long id;
    private User user;
    private String type;
    private String title;
    private String message;
    private boolean read;
    private LocalDate createdAt;
}
