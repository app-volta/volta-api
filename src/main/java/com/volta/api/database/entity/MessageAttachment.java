package com.volta.api.database.entity;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageAttachment {
    private Long id;
    private Message message;
    private String fileUrl;
    private String fileType;
}
