package com.volta.api.mapper;

import com.volta.api.database.entity.Users;
import com.volta.api.dto.response.UserResponseDTO;

public class UserMapper {
    public UserResponseDTO toResponse(Users user){
        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPosition(),
                user.getCompany().getId(),
                user.getRole().getType()
        );
    }
}
