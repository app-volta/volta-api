package com.volta.api.config;

import com.volta.api.database.entity.Company;
import com.volta.api.database.entity.Role;
import com.volta.api.database.entity.Users;
import com.volta.api.database.repository.CompanyRepository;
import com.volta.api.database.repository.RoleRepository;
import com.volta.api.database.repository.UserRepository;
import com.volta.api.enums.RoleType;
import com.volta.api.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AdminBootstrap implements ApplicationRunner {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;
    private final AdminProperties adminProperties;

    @Override
    public void run(ApplicationArguments args) {
        if (adminProperties.email().isBlank() || adminProperties.password().isBlank() || adminProperties.companyId().isBlank())
            return;
        if (userRepository.findByEmail(adminProperties.email()).isPresent()) return;

        Company company = companyRepository.findById(UUID.fromString(adminProperties.companyId()))
                .orElseThrow(() -> new ResourceNotFoundException("Company"));
        Role role = roleRepository.findByType(RoleType.ADMIN.name())
                .orElseThrow(() -> new IllegalStateException("Role ADMIN not found"));

        userRepository.save(
                Users.builder()
                        .name("Admin")
                        .email(adminProperties.email())
                        .role(role)
                        .company(company)
                        .passwordHash(passwordEncoder.encode(adminProperties.password()))
                        .build()
        );
    }
}
