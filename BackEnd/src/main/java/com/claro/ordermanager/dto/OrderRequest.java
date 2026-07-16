package com.claro.ordermanager.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record OrderRequest(

        @NotBlank(message = "Customer name is required")
        @Size(
                min = 5,
                message = "Customer name must contain at least 5 characters"
        )
        String displayName,

        @NotNull(message = "Item quantity is required")
        @Min(
                value = 1,
                message = "Item quantity must be greater than zero"
        )
        Integer items,

        @NotNull(message = "Weight is required")
        @Min(
                value = 1,
                message = "Weight must be greater than zero"
        )
        Integer weight
) {
}