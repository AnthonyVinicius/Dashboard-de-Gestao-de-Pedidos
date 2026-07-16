package com.claro.ordermanager.dto;

import com.claro.ordermanager.entity.OrderStatus;

import java.util.UUID;

public record OrderResponse(
        UUID id,
        String displayName,
        Integer items,
        Integer weight,
        OrderStatus status
) {
}