package com.claro.ordermanager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserUpdateDTO(

        @NotBlank(message = "Name is required")
        @Size(
                min = 5,
                max = 50,
                message = "Name must be between 5 and 50 characters"
        )
        String name

) {}