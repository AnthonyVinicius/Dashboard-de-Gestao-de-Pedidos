package com.claro.ordermanager.service;

import com.claro.ordermanager.dto.UserCreateDTO;
import com.claro.ordermanager.dto.UserResponseDTO;
import com.claro.ordermanager.dto.UserUpdateDTO;

import java.util.List;
import java.util.UUID;

public interface UserService {
    List<UserResponseDTO> getAll();
    UserResponseDTO getById(UUID id);
    UserResponseDTO create(UserCreateDTO dto);
    UserResponseDTO update(UUID id, UserUpdateDTO dto);
    void delete(UUID id);
}
