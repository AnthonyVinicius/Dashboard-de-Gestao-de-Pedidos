package com.claro.pedidos.dto;

import com.claro.pedidos.entity.StatusPedido;
import jakarta.validation.constraints.NotNull;

public record AtualizarStatusRequest(

        @NotNull(message = "O novo status é obrigatório")
        StatusPedido status
) {
}