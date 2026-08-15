package com.volta.api.database.entity;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class AiReport {
    private Long id;
    private Incident incident;
    private String detectedWasteType;
    private String aiContaminationLevel;
    private String recommendations;
    private String reportText;
    private LocalDate generatedAt;
}
