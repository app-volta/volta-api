package com.volta.api.support;

import com.volta.api.database.entity.Company;
import com.volta.api.database.entity.Role;
import com.volta.api.dto.user.UserRequestDTO;
import com.volta.api.security.AuthenticatedUser;

import java.util.UUID;

public final class UserFixtures {

    private UserFixtures() {
    }

    public static UserRequestDTO.UserRequestDTOBuilder userRequest(UUID companyId) {
        return UserRequestDTO.builder()
                .name("Ana Souza")
                .email("ana@volta.com")
                .password("password1234")
                .position("Analyst")
                .companyId(companyId);
    }

    public static Company company(UUID id) {
        Company company = new Company();
        company.setId(id);
        return company;
    }

    public static Role role(String type) {
        Role role = new Role();
        role.setId(UUID.randomUUID());
        role.setType(type);
        return role;
    }

    public static AuthenticatedUser authenticatedUser(UUID id, UUID companyId, String role) {
        return new AuthenticatedUser(id, "user@volta.com", null, companyId, role);
    }
}
