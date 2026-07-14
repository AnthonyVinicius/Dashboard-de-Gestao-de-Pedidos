package com.claro.ordermanager.mapper;

import com.claro.ordermanager.dto.UserResponseDTO;
import com.claro.ordermanager.entity.User;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserMapper {

    public UserResponseDTO toDTO(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }
}
