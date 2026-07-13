package com.claro.pedidos.dto;

import com.claro.pedidos.entity.StatusPedido;

import java.util.UUID;

public record PedidoResponse(
        UUID id,
        String displayName,
        Integer itens,
        Integer peso,
        StatusPedido status
) {
}