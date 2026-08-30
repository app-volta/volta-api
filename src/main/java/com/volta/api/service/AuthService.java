package com.volta.api.service;

import com.volta.api.database.entity.Role;
import com.volta.api.database.entity.Users;
import com.volta.api.database.repository.RoleRepository;
import com.volta.api.database.repository.UserRepository;
import com.volta.api.dto.LoginRequestDTO;
import com.volta.api.dto.RegisterRequestDTO;
import com.volta.api.dto.TokenResponseDTO;
import com.volta.api.enums.RoleTypeEnum;
import com.volta.api.security.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;

    @Value("${jwt.expiration}")
    private long expirationTime;

    public void register(RegisterRequestDTO dto) throws BadRequestException {
        Users user = userRepository.findByEmail(dto.email()).orElse(null);

        if (user != null){
            throw new BadRequestException();
        }

        Role role = roleRepository.findByType(RoleTypeEnum.EMPLOYEE.name())
                .orElseGet(() -> roleRepository.save(Role.builder()
                        .type(RoleTypeEnum.EMPLOYEE.name())
                        .build()
                ));

        userRepository.save(
                Users.builder()
                        .name(dto.name())
                        .email(dto.email())
                        .role(role)
                        .passwordHash(passwordEncoder.encode(dto.password()))
                        .build()
        );
    }

    public TokenResponseDTO login(LoginRequestDTO dto) throws Exception {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.email(), dto.password()));

            //authentication provider -> UserDetailsService -> passwordEncoder.matches() -> autenticado

            String token = tokenProvider.gerarToken(authentication);
            return new TokenResponseDTO(token, expirationTime);
        } catch (Exception e){
            throw e;
        }
    }
}
