package com.volta.api.usecase;

import com.volta.api.dto.request.UserRequestDTO;
import com.volta.api.dto.response.UserResponseDTO;

public interface UserUseCase {
    UserResponseDTO register(UserRequestDTO dto);
}
