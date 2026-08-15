package com.volta.api.database.entity;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Attachment {
    private Long id;
    private Incident incident;
    private String fileUrl;
    private String fileType;
}
