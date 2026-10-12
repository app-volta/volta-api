package com.volta.api.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("AuthenticatedUser")
class AuthenticatedUserTest {

    @Test
    @DisplayName("toString não expõe o hash da senha")
    void toString_doesNotExposePasswordHash() {
        AuthenticatedUser user = new AuthenticatedUser(
                UUID.randomUUID(), "ana@volta.com", "secret-bcrypt-hash", UUID.randomUUID(), "ADMIN");

        assertThat(user.toString())
                .doesNotContain("secret-bcrypt-hash")
                .contains("ana@volta.com")
                .contains("ADMIN");
    }

    @Test
    @DisplayName("expõe a role como authority e o e-mail como username")
    void authoritiesAndUsername_comeFromRoleAndEmail() {
        AuthenticatedUser user = new AuthenticatedUser(
                UUID.randomUUID(), "ana@volta.com", null, UUID.randomUUID(), "MANAGER");

        assertThat(user.getUsername()).isEqualTo("ana@volta.com");
        assertThat(user.getAuthorities()).extracting("authority").containsExactly("MANAGER");
    }
}
