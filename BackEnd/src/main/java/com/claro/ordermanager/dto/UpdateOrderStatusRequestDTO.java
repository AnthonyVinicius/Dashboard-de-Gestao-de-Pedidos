package com.claro.ordermanager.dto;

import com.claro.ordermanager.entity.OrderStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateOrderStatusRequestDTO(
        @NotNull(message = "New status is required")
        OrderStatus status
) {
}