package com.volta.api.service;

import com.volta.api.dto.auth.LoginRequestDTO;
import com.volta.api.dto.auth.TokenResponseDTO;
import com.volta.api.security.jwt.TokenProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthService")
class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private TokenProvider tokenProvider;

    @InjectMocks
    private AuthService authService;

    @Test
    @DisplayName("login com credenciais válidas devolve o token e a expiração")
    void login_validCredentials_returnsTokenAndExpiration() {
        Authentication authenticated = mock(Authentication.class);
        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(authenticated);
        when(tokenProvider.generateToken(authenticated)).thenReturn("fake-token");
        when(tokenProvider.getExpirationTime()).thenReturn(3_600_000L);

        TokenResponseDTO response = authService.login(new LoginRequestDTO("ana@volta.com", "password123"));

        assertThat(response.token()).isEqualTo("fake-token");
        assertThat(response.expiresIn()).isEqualTo(3_600_000L);

        ArgumentCaptor<Authentication> captor = ArgumentCaptor.forClass(Authentication.class);
        verify(authenticationManager).authenticate(captor.capture());
        assertThat(captor.getValue().getPrincipal()).isEqualTo("ana@volta.com");
        assertThat(captor.getValue().getCredentials()).isEqualTo("password123");
    }

    @Test
    @DisplayName("login com senha errada propaga a exceção e não gera token")
    void login_wrongPassword_propagatesExceptionAndDoesNotGenerateToken() {
        when(authenticationManager.authenticate(any(Authentication.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        assertThatThrownBy(() -> authService.login(new LoginRequestDTO("ana@volta.com", "wrong")))
                .isInstanceOf(BadCredentialsException.class);

        verifyNoInteractions(tokenProvider);
    }
}
