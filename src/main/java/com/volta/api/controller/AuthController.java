package com.volta.api.controller;

import com.volta.api.dto.LoginRequestDTO;
import com.volta.api.dto.RegisterRequestDTO;
import com.volta.api.dto.TokenResponseDTO;
import com.volta.api.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authenticationService;

    @PostMapping("/register")
    public void register(@RequestBody RegisterRequestDTO registerRequestDto) throws Exception{
        authenticationService.register(registerRequestDto);
    }

    @PostMapping("/login")
    public TokenResponseDTO login(@RequestBody LoginRequestDTO loginRequestDto) throws Exception{
        return authenticationService.login(loginRequestDto);
    }
}