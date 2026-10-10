package com.volta.api.security.jwt;

import com.volta.api.security.AuthenticatedUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("TokenProvider")
class TokenProviderTest {

    private static final String TEST_KEY = "test-only-key-with-more-than-32-bytes-long";

    private TokenProvider tokenProvider;
    private AuthenticatedUser user;

    @BeforeEach
    void setUp() {
        tokenProvider = new TokenProvider();
        ReflectionTestUtils.setField(tokenProvider, "key", TEST_KEY);
        ReflectionTestUtils.setField(tokenProvider, "expirationTime", 3_600_000L);

        user = new AuthenticatedUser(
                UUID.randomUUID(), "ana@volta.com", "any-hash", UUID.randomUUID(), "ADMIN");
    }

    private Authentication authenticationOf(AuthenticatedUser authenticatedUser) {
        return new UsernamePasswordAuthenticationToken(authenticatedUser, null, authenticatedUser.getAuthorities());
    }

    @Test
    @DisplayName("gera um token válido com os dados do usuário")
    void generateToken_validUser_returnsValidTokenWithUserClaims() {
        String token = tokenProvider.generateToken(authenticationOf(user));

        assertThat(token).isNotBlank();
        assertThat(tokenProvider.isTokenValid(token)).isTrue();
        assertThat(tokenProvider.getUsername(token)).isEqualTo("ana@volta.com");

        Claims claims = tokenProvider.getClaims(token);
        assertThat(claims.get("userId", String.class)).isEqualTo(user.id().toString());
        assertThat(claims.get("companyId", String.class)).isEqualTo(user.companyId().toString());
        assertThat(claims.get("role", String.class)).isEqualTo("ADMIN");
    }

    @Test
    @DisplayName("define a expiração posterior à emissão")
    void generateToken_validUser_setsExpirationAfterIssuedAt() {
        String token = tokenProvider.generateToken(authenticationOf(user));

        Claims claims = tokenProvider.getClaims(token);
        assertThat(claims.getExpiration()).isAfter(claims.getIssuedAt());
    }

    @Test
    @DisplayName("rejeita token assinado com outra chave")
    void isTokenValid_tokenSignedWithAnotherKey_returnsFalse() {
        String forgedToken = Jwts.builder()
                .subject("ana@volta.com")
                .signWith(Keys.hmacShaKeyFor(
                        "another-completely-different-key-32-bytes".getBytes(StandardCharsets.UTF_8)))
                .compact();

        assertThat(tokenProvider.isTokenValid(forgedToken)).isFalse();
    }

    @Test
    @DisplayName("rejeita token expirado")
    void isTokenValid_expiredToken_returnsFalse() {
        ReflectionTestUtils.setField(tokenProvider, "expirationTime", -1_000L);
        String expiredToken = tokenProvider.generateToken(authenticationOf(user));

        assertThat(tokenProvider.isTokenValid(expiredToken)).isFalse();
    }

    @Test
    @DisplayName("rejeita texto que não é um JWT")
    void isTokenValid_textThatIsNotJwt_returnsFalse() {
        assertThat(tokenProvider.isTokenValid("this-is-not-a-jwt")).isFalse();
        assertThat(tokenProvider.isTokenValid("")).isFalse();
    }
}
