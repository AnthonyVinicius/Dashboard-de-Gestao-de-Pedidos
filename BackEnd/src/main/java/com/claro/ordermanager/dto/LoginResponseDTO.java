package com.claro.ordermanager.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginResponseDTO(
        String token
)
{}
