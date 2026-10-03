package com.volta.api.service;

import com.volta.api.database.entity.Company;
import com.volta.api.database.entity.Role;
import com.volta.api.database.entity.Users;
import com.volta.api.database.repository.CompanyRepository;
import com.volta.api.database.repository.RoleRepository;
import com.volta.api.database.repository.UserRepository;
import com.volta.api.dto.request.UserRequestDTO;
import com.volta.api.dto.request.update.UserRoleUpdateRequestDTO;
import com.volta.api.dto.request.update.UserUpdateRequestDTO;
import com.volta.api.dto.response.UserResponseDTO;
import com.volta.api.enums.RoleType;
import com.volta.api.exception.BusinessRuleException;
import com.volta.api.exception.ConflictException;
import com.volta.api.exception.ResourceNotFoundException;
import com.volta.api.mapper.UserMapper;
import com.volta.api.security.AuthenticatedUser;
import com.volta.api.usecase.UserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

        Role role = roleRepository.findByType(RoleType.EMPLOYEE.name())
                .orElseThrow(() -> new  ResourceNotFoundException("Role"));

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

    public List<UserResponseDTO> getUsers() {
        List<UserResponseDTO> users = new ArrayList<>();

        for (Users user : userRepository.findAll()) {
            users.add(userMapper.toResponse(user));
        }

        return users;
    }

    public UserResponseDTO getUserById(UUID id) {
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User"));
        return userMapper.toResponse(user);
    }

    @Transactional
    public UserResponseDTO update(UUID id, UserUpdateRequestDTO dto) {
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User"));
        userMapper.updateEntity(user, dto);
        userRepository.save(user);
        return userMapper.toResponse(user);
    }

    @Transactional
    public UserResponseDTO updateRole(UUID id, UserRoleUpdateRequestDTO dto, AuthenticatedUser author) {
        if (id.equals(author.id())){
            throw new BusinessRuleException("You cannot change your own role");
        }
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User"));

        Role role = roleRepository.findByType(dto.role().name())
                .orElseThrow(() -> new ResourceNotFoundException("Role"));
        user.setRole(role);
        userRepository.save(user);

        return userMapper.toResponse(user);
    }
}
