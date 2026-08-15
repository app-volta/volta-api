package com.volta.api.database.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    private String name;

    private String email;

    @JsonIgnore
    @Column(name = "password_hash")
    private String passwordHash;

    private String role;

    @Column(name = "user_type")
    private String userType;

    @OneToMany(mappedBy = "Incident")
    private Set<Incident> incidents = new HashSet<>();

    @OneToMany(mappedBy = "Message")
    private Set<Message> messages = new HashSet<>();

    @OneToMany(mappedBy = "Notification")
    private Set<Notification> notifications = new HashSet<>();

    @OneToMany(mappedBy = "Review")
    private Set<Review> reviews = new HashSet<>();
}
