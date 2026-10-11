package com.volta.api.usecase;

import com.volta.api.dto.auth.LoginRequestDTO;
import com.volta.api.dto.auth.TokenResponseDTO;

public interface AuthUseCase {
    TokenResponseDTO login(LoginRequestDTO loginRequestDto);
}
