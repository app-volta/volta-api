package com.volta.api.service;

import com.volta.api.database.entity.Company;
import com.volta.api.database.entity.Role;
import com.volta.api.database.entity.Users;
import com.volta.api.database.repository.CompanyRepository;
import com.volta.api.database.repository.RoleRepository;
import com.volta.api.database.repository.UserRepository;
import com.volta.api.dto.request.UserRequestDTO;
import com.volta.api.dto.response.UserResponseDTO;
import com.volta.api.enums.RoleTypeEnum;
import com.volta.api.exception.ConflictException;
import com.volta.api.exception.ResourceNotFoundException;
import com.volta.api.mapper.UserMapper;
import com.volta.api.usecase.UserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserUseCase {

    private final UserRepository userRepository;

    private final CompanyRepository companyRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserMapper userMapper;

    public UserResponseDTO register(UserRequestDTO dto) {
        userRepository.findByEmail(dto.email())
                .ifPresent(u -> {
                    throw new ConflictException("Email already registered");
                });


        Company company = companyRepository.findById(dto.companyId())
                .orElseThrow(() -> new ResourceNotFoundException("Company"));

        Role role = roleRepository.findByType(RoleTypeEnum.EMPLOYEE.name())
                .orElseGet(() -> roleRepository.save(
                        Role.builder()
                                .type(RoleTypeEnum.EMPLOYEE.name())
                                .build()
                ));

        Users saved = userRepository.save(
                Users.builder()
                        .name(dto.name())
                        .email(dto.email())
                        .position(dto.position())
                        .role(role)
                        .company(company)
                        .passwordHash(passwordEncoder.encode(dto.password()))
                        .build()
        );

        return userMapper.toResponse(saved);
    }
}
