package com.claro.ordermanager.dto;


import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record UserResponseDTO(

        @NotBlank
        @Id
        UUID id,

        @NotBlank
        String name,

        @NotBlank
        @Email
        String email

) {}
