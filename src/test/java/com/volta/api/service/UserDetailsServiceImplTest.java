package com.volta.api.service;

import com.volta.api.database.entity.Users;
import com.volta.api.database.repository.UserRepository;
import com.volta.api.security.AuthenticatedUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;
import java.util.UUID;

import static com.volta.api.support.UserFixtures.company;
import static com.volta.api.support.UserFixtures.role;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserDetailsServiceImpl")
class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl service;

    @Test
    @DisplayName("e-mail cadastrado devolve o usuário autenticável com empresa e perfil")
    void loadUserByUsername_existingEmail_returnsAuthenticatedUser() {
        UUID userId = UUID.randomUUID();
        UUID companyId = UUID.randomUUID();
        Users user = Users.builder()
                .id(userId)
                .email("ana@volta.com")
                .passwordHash("encoded-hash")
                .company(company(companyId))
                .role(role("MANAGER"))
                .build();
        when(userRepository.findByEmail("ana@volta.com")).thenReturn(Optional.of(user));

        UserDetails details = service.loadUserByUsername("ana@volta.com");

        assertThat(details).isInstanceOf(AuthenticatedUser.class);
        AuthenticatedUser authenticated = (AuthenticatedUser) details;
        assertThat(authenticated.id()).isEqualTo(userId);
        assertThat(authenticated.companyId()).isEqualTo(companyId);
        assertThat(authenticated.getUsername()).isEqualTo("ana@volta.com");
        assertThat(authenticated.getPassword()).isEqualTo("encoded-hash");
        assertThat(authenticated.getAuthorities()).extracting("authority").containsExactly("MANAGER");
    }

    @Test
    @DisplayName("e-mail desconhecido lança UsernameNotFoundException")
    void loadUserByUsername_unknownEmail_throwsUsernameNotFound() {
        when(userRepository.findByEmail("nobody@volta.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.loadUserByUsername("nobody@volta.com"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("User not found");
    }
}
