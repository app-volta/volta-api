package com.volta.api.service;

import com.volta.api.dto.request.LoginRequestDTO;
import com.volta.api.dto.response.TokenResponseDTO;
import com.volta.api.security.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;

    @Value("${jwt.expiration}")
    private long expirationTime;

    public TokenResponseDTO login(LoginRequestDTO dto){
        Authentication authentication = authenticationManager
                .authenticate(
                        new UsernamePasswordAuthenticationToken(
                                dto.email(),
                                dto.password()
                        )
                );
        String token = tokenProvider.gerarToken(authentication);
        return new TokenResponseDTO(token, expirationTime);
    }
}
