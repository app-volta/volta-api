package com.volta.api.dto.request.update;

import com.volta.api.enums.RoleType;
import jakarta.validation.constraints.NotNull;

public record UserRoleUpdateRequestDTO(

        @NotNull(message = "The role is mandatory")
        RoleType role

) {}
