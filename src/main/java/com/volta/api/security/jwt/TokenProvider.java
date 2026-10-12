package com.volta.api.security.jwt;

import com.volta.api.security.AuthenticatedUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Optional;

@Component
public class TokenProvider {
    private final long expirationTime;
    private final SecretKey signingKey;

    public TokenProvider(
            @Value("${jwt.key}") String key,
            @Value("${jwt.expiration}") long expirationTime
    ) {
        this.signingKey = Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));
        this.expirationTime = expirationTime;
    }

    public String generateToken(Authentication authentication) {
        AuthenticatedUser user = (AuthenticatedUser) authentication.getPrincipal();
        Date now = new Date();
        Date expiration = new Date(now.getTime() + expirationTime);

        return Jwts.builder()
                .subject(user.getUsername())
                .claim("userId", user.id().toString())
                .claim("companyId", user.companyId().toString())
                .claim("role", user.role())
                .issuedAt(now)
                .expiration(expiration)
                .signWith(signingKey)
                .compact();
    }

    public long getExpirationTime() {
        return expirationTime;
    }

    public boolean isTokenValid(String token) {
        return parseClaims(token).isPresent();
    }

    public String getUsername(String token) {
        return getClaims(token).getSubject();
    }

    public Optional<Claims> parseClaims(String token) {
        try {
            return Optional.of(getClaims(token));
        } catch (JwtException | IllegalArgumentException exception) {
            return Optional.empty();
        }
    }

    public Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
