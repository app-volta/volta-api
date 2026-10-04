package com.volta.api.usecase;

import com.volta.api.dto.request.LoginRequestDTO;
import com.volta.api.dto.response.TokenResponseDTO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;

public interface AuthUseCase {
    TokenResponseDTO login(@Valid @RequestBody LoginRequestDTO loginRequestDto);
}
