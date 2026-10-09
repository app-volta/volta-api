package com.volta.api.controller;

import com.volta.api.controller.docs.AuthControllerDocs;
import com.volta.api.dto.request.LoginRequestDTO;
import com.volta.api.dto.response.TokenResponseDTO;
import com.volta.api.usecase.AuthUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController implements AuthControllerDocs {

    private final AuthUseCase authUseCase;

    @PostMapping("/login")
    public TokenResponseDTO login(@Valid @RequestBody LoginRequestDTO loginRequestDto) {
        return authUseCase.login(loginRequestDto);
    }
}