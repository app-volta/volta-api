package com.volta.api.database.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "conversation_id")
    private Conversation conversation;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String text;

    private boolean reported;

    @Column(name = "sent_at")
    private LocalDate sentAt;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @OneToMany(mappedBy = "message_attachment")
    private Set<MessageAttachment> messageAttachments = new HashSet<>();
}
