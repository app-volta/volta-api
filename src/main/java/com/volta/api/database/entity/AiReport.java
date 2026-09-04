package com.volta.api.database.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "ai_report")
public class AiReport {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(
            columnDefinition = "UUID DEFAULT gen_random_uuid()",
            updatable = false,
            nullable = false,
            unique = true
    )
    private UUID id;

    @NotNull
    @OneToOne
    @JoinColumn(name = "incident_id", nullable = false)
    private Incident incident;

    @Column(name = "detected_waste_type", length = 100)
    private String detectedWasteType;

    @Column(name = "ai_contamination_level", length = 50)
    private String aiContaminationLevel;

    private String recommendations;

    @Column(name = "report_text")
    private String reportText;

    @NotNull
    @Column(
            name = "generated_at",
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP",
            nullable = false
    )
    private LocalDateTime generatedAt;
}
