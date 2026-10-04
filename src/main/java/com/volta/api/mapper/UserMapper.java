package com.volta.api.mapper;

import com.volta.api.database.entity.Users;
import com.volta.api.dto.request.update.UserUpdateRequestDTO;
import com.volta.api.dto.response.UserResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserResponseDTO toResponse(Users user) {
        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPosition(),
                user.getCompany().getId(),
                user.getRole().getType()
        );
    }

    public void updateEntity(Users user, UserUpdateRequestDTO dto) {
        user.setName(dto.name());
        user.setPosition(dto.position());
    }
}
