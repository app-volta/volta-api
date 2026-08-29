package com.volta.api.database.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Incident {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "area_id")
    private Area area;

    @ManyToOne
    @JoinColumn(name = "waste_type_id")
    private WasteType wasteType;

    @Column(name = "photo_url")
    private String photoUrl;

    @Column(name = "employee_description")
    private String employeeDescription;

    @Column(name = "contamination_level")
    private String contaminationLevel;

    @Column(name = "estimated_quantity")
    private String estimatedQuantity;

    private String priority;

    private String status;

    @Column(name = "registered_at")
    private LocalDateTime registeredAt;

    @OneToMany(mappedBy = "incident")
    private Set<Attachment> attachments = new HashSet<>();

    @OneToMany(mappedBy = "incident")
    private Set<Collection> collections = new HashSet<>();
}
