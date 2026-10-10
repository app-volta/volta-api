package com.volta.api.usecase;

import com.volta.api.dto.auth.LoginRequestDTO;
import com.volta.api.dto.auth.TokenResponseDTO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;

public interface AuthUseCase {
    TokenResponseDTO login(@Valid @RequestBody LoginRequestDTO loginRequestDto);
}
