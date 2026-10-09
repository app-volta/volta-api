package com.volta.api.dto.request.update;

import com.volta.api.enums.RoleType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Novo perfil de acesso do usuário")
public record UserRoleUpdateRequestDTO(

        @Schema(description = "Perfil de acesso", example = "MANAGER")
        @NotNull(message = "The role is mandatory")
        RoleType role

) {
}
