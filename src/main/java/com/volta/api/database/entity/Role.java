package com.volta.api.database.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Role implements GrantedAuthority {
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
    @Column(length = 50, unique = true, nullable = false)
    private String type;

    @OneToMany(mappedBy = "role")
    private Set<Users> users = new HashSet<>();

    @Override
    public @Nullable String getAuthority() {
        return type;
    }
}
