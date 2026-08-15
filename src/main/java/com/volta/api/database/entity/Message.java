package com.volta.api.database.entity;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Message {
    private Long id;
    private Conversation conversation;
    private Cooperative cooperative;
    private Collection collection;
    private LocalDate createdAt;
}
