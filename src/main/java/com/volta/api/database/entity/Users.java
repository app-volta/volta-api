package com.volta.api.database.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDate;
import java.util.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cooperative_id")
    private Cooperative cooperative;

    @NotNull
    @Column(length = 150, nullable = false)
    private String name;

    @NotNull
    @Column(unique = true, length = 150)
    private String email;

    @NotNull
    @JsonIgnore
    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(length = 100)
    private String position;

    @Column(name = "photo_url", length = 2048)
    private String photoUrl;

    @Column(name = "registered_at")
    private LocalDate registeredAt;

    @Column(name = "first_login")
    private Boolean firstLogin;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @Builder.Default
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Set<Incident> incidents = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Set<Review> reviews = new HashSet<>();
}