package com.volta.api.security.filter;

import com.volta.api.security.AuthenticatedUser;
import com.volta.api.security.jwt.TokenProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
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
        if (StringUtils.hasText(authorizationHeader) && authorizationHeader.startsWith("Bearer ")){
            String token = authorizationHeader.substring(7);

            //validar token
            if (tokenProvider.isTokenValid(token)){
                try {
                    Claims claims = tokenProvider.getClaims(token);

                    String uid = claims.get("userId", String.class);
                    String subject = claims.getSubject();
                    String companyId = claims.get("companyId", String.class);
                    String role = claims.get("role", String.class);

                    UUID userId = UUID.fromString(uid);
                    UUID companyUUID = UUID.fromString(companyId);

                    AuthenticatedUser principal = new AuthenticatedUser(
                            userId,
                            subject,
                            null,
                            companyUUID,
                            role
                    );

                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(auth);
                } catch (JwtException e) {
                    // token inválido ou expirado
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}
