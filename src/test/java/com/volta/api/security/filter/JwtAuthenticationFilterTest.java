package com.volta.api.security.filter;

import com.volta.api.security.AuthenticatedUser;
import com.volta.api.security.jwt.TokenProvider;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("JwtAuthenticationFilter")
class JwtAuthenticationFilterTest {

    private static final String TEST_KEY = "test-only-key-with-more-than-32-bytes-long";

    private TokenProvider tokenProvider;
    private JwtAuthenticationFilter filter;

    @BeforeEach
    void setUp() {
        tokenProvider = new TokenProvider(TEST_KEY, 3_600_000L);
        filter = new JwtAuthenticationFilter(tokenProvider);
        SecurityContextHolder.clearContext();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    private Authentication runFilterWith(String authorizationHeader) throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        if (authorizationHeader != null) {
            request.addHeader("Authorization", authorizationHeader);
        }
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(request, new MockHttpServletResponse(), chain);

        assertThat(chain.getRequest()).isNotNull();
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @Test
    @DisplayName("token válido autentica o usuário com id, empresa e role")
    void doFilter_validToken_authenticatesUser() throws Exception {
        AuthenticatedUser user = new AuthenticatedUser(
                UUID.randomUUID(), "ana@volta.com", null, UUID.randomUUID(), "MANAGER");
        String token = tokenProvider.generateToken(
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()));

        Authentication authentication = runFilterWith("Bearer " + token);

        assertThat(authentication).isNotNull();
        AuthenticatedUser principal = (AuthenticatedUser) authentication.getPrincipal();
        assertThat(principal.id()).isEqualTo(user.id());
        assertThat(principal.companyId()).isEqualTo(user.companyId());
        assertThat(authentication.getAuthorities()).extracting("authority").containsExactly("MANAGER");
    }

    @Test
    @DisplayName("sem header segue a cadeia sem autenticar")
    void doFilter_missingHeader_doesNotAuthenticate() throws Exception {
        assertThat(runFilterWith(null)).isNull();
    }

    @Test
    @DisplayName("token inválido segue a cadeia sem autenticar")
    void doFilter_invalidToken_doesNotAuthenticate() throws Exception {
        assertThat(runFilterWith("Bearer not-a-jwt")).isNull();
    }

    @Test
    @DisplayName("token sem as claims obrigatórias não autentica")
    void doFilter_tokenWithoutClaims_doesNotAuthenticate() throws Exception {
        String token = Jwts.builder()
                .subject("ana@volta.com")
                .signWith(Keys.hmacShaKeyFor(TEST_KEY.getBytes(StandardCharsets.UTF_8)))
                .compact();

        assertThat(runFilterWith("Bearer " + token)).isNull();
    }

    @Test
    @DisplayName("token com UUID malformado não autentica")
    void doFilter_tokenWithMalformedUuid_doesNotAuthenticate() throws Exception {
        String token = Jwts.builder()
                .subject("ana@volta.com")
                .claim("userId", "not-a-uuid")
                .claim("companyId", UUID.randomUUID().toString())
                .claim("role", "ADMIN")
                .signWith(Keys.hmacShaKeyFor(TEST_KEY.getBytes(StandardCharsets.UTF_8)))
                .compact();

        assertThat(runFilterWith("Bearer " + token)).isNull();
    }

    @Test
    @DisplayName("header que não é Bearer não autentica")
    void doFilter_nonBearerHeader_doesNotAuthenticate() throws Exception {
        assertThat(runFilterWith("Basic dXNlcjpwYXNz")).isNull();
    }

    @Test
    @DisplayName("token sem companyId não autentica")
    void doFilter_tokenWithoutCompanyId_doesNotAuthenticate() throws Exception {
        String token = Jwts.builder()
                .subject("ana@volta.com")
                .claim("userId", UUID.randomUUID().toString())
                .claim("role", "ADMIN")
                .signWith(Keys.hmacShaKeyFor(TEST_KEY.getBytes(StandardCharsets.UTF_8)))
                .compact();

        assertThat(runFilterWith("Bearer " + token)).isNull();
    }

    @Test
    @DisplayName("token sem role não autentica")
    void doFilter_tokenWithoutRole_doesNotAuthenticate() throws Exception {
        String token = Jwts.builder()
                .subject("ana@volta.com")
                .claim("userId", UUID.randomUUID().toString())
                .claim("companyId", UUID.randomUUID().toString())
                .signWith(Keys.hmacShaKeyFor(TEST_KEY.getBytes(StandardCharsets.UTF_8)))
                .compact();

        assertThat(runFilterWith("Bearer " + token)).isNull();
    }
}
