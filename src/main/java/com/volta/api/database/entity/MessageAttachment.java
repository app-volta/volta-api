package com.volta.api.database.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "message_attachment")
public class MessageAttachment {

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
    @ManyToOne
    @JoinColumn(name = "message_id", nullable = false)
    private Message message;

    @NotNull
    @Column(name = "file_url", length = 500, nullable = false)
    private String fileUrl;

    @NotNull
    @Column(name = "file_type", length = 500, nullable = false)
    private String fileType;
}
