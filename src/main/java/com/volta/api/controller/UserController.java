package com.volta.api.controller;

import com.volta.api.dto.request.UserRequestDTO;
import com.volta.api.dto.request.update.UserRoleUpdateRequestDTO;
import com.volta.api.dto.request.update.UserUpdateRequestDTO;
import com.volta.api.dto.response.UserResponseDTO;
import com.volta.api.security.AuthenticatedUser;
import com.volta.api.usecase.UserUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<UserResponseDTO>> show(){
        List<UserResponseDTO> users = userUseCase.getUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> showMe(@AuthenticationPrincipal AuthenticatedUser author){
        UserResponseDTO user = userUseCase.getUserById(author.id());
        return ResponseEntity.ok(user);
    }

    @PutMapping("/me")
    public ResponseEntity<UserResponseDTO> update(
            @Valid @RequestBody UserUpdateRequestDTO dto,
            @AuthenticationPrincipal AuthenticatedUser author
    ){
        UserResponseDTO response = userUseCase.update(author.id(), dto);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/role")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<UserResponseDTO> updateRole(
            @PathVariable UUID id,
            @Valid @RequestBody UserRoleUpdateRequestDTO dto,
            @AuthenticationPrincipal AuthenticatedUser author
    ){
        UserResponseDTO response = userUseCase.updateRole(id, dto, author);
        return ResponseEntity.ok(response);
    }
}
