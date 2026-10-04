package com.volta.api.usecase;

import com.volta.api.dto.request.UserRequestDTO;
import com.volta.api.dto.request.update.UserRoleUpdateRequestDTO;
import com.volta.api.dto.request.update.UserUpdateRequestDTO;
import com.volta.api.dto.response.UserResponseDTO;
import com.volta.api.security.AuthenticatedUser;

import java.util.List;
import java.util.UUID;

public interface UserUseCase {
    UserResponseDTO register(UserRequestDTO dto);

    List<UserResponseDTO> getUsers();

    UserResponseDTO getUserById(UUID id);

    UserResponseDTO update(UUID id, UserUpdateRequestDTO dto);

    UserResponseDTO updateRole(UUID id, UserRoleUpdateRequestDTO dto, AuthenticatedUser author);
}
