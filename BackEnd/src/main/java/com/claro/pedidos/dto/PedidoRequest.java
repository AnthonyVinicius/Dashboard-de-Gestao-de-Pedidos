package com.claro.pedidos.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PedidoRequest(

        @NotBlank(message = "O nome do cliente é obrigatório")
        @Size(
                min = 5,
                message = "O nome do cliente deve ter no mínimo 5 caracteres"
        )
        String displayName,

        @NotNull(message = "A quantidade de itens é obrigatória")
        @Min(
                value = 1,
                message = "A quantidade de itens deve ser maior que zero"
        )
        Integer itens,

        @NotNull(message = "O peso é obrigatório")
        @Min(
                value = 1,
                message = "O peso deve ser maior que zero"
        )
        Integer peso
) {
}