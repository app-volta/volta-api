package com.volta.api.controller;

import com.volta.api.dto.request.UserRequestDTO;
import com.volta.api.dto.response.UserResponseDTO;
import com.volta.api.usecase.UserUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserUseCase userUseCase;
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<UserResponseDTO> create(@Valid @RequestBody UserRequestDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(userUseCase.register(dto));
    }

}
