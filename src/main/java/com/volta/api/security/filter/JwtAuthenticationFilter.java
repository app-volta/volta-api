package com.volta.api.security.filter;

import com.volta.api.security.AuthenticatedUser;
import com.volta.api.security.jwt.TokenProvider;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");

        if (StringUtils.hasText(authorizationHeader) && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring("Bearer ".length());
            tokenProvider.parseClaims(token)
                    .flatMap(this::toPrincipal)
                    .ifPresent(principal -> SecurityContextHolder.getContext().setAuthentication(
                            new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities())
                    ));
        }

        filterChain.doFilter(request, response);
    }

    private Optional<AuthenticatedUser> toPrincipal(Claims claims) {
        String userId = claims.get("userId", String.class);
        String companyId = claims.get("companyId", String.class);
        String role = claims.get("role", String.class);

        if (!StringUtils.hasText(userId) || !StringUtils.hasText(companyId) || !StringUtils.hasText(role)) {
            return Optional.empty();
        }

        try {
            return Optional.of(
                    new AuthenticatedUser(
                            UUID.fromString(userId),
                            claims.getSubject(),
                            null,
                            UUID.fromString(companyId),
                            role
                    )
            );
        } catch (IllegalArgumentException exception) {
            return Optional.empty();
        }
    }
}
