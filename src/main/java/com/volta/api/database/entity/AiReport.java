package com.volta.api.database.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "ai_report")
public class AiReport {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "incident_id")
    private Incident incident;

    @Column(name = "detected_waste_type")
    private String detectedWasteType;

    @Column(name = "ai_contamination_level")
    private String aiContaminationLevel;

    private String recommendations;

    @Column(name = "report_text")
    private String reportText;

    @Column(name = "generated_at")
    private LocalDate generatedAt;
}
