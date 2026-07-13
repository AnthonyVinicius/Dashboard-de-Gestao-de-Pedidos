package com.claro.pedidos.controller;

import com.claro.pedidos.dto.PedidoRequest;
import com.claro.pedidos.dto.PedidoResponse;
import com.claro.pedidos.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<List<PedidoResponse>> getAllPedidos() {
        return ResponseEntity.ok(
                orderService.getAllPedidos()
        );
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<PedidoResponse> getPedidoById(
            @PathVariable UUID uuid
    ) {
        return ResponseEntity.ok(
                orderService.getPedidoById(uuid)
        );
    }

    @PostMapping
    public ResponseEntity<PedidoResponse> createPedido(
            @Valid @RequestBody PedidoRequest request
    ) {
        PedidoResponse pedidoCriado =
                orderService.createPedido(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(pedidoCriado);
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> deletePedido(
            @PathVariable UUID uuid
    ) {
        orderService.deletePedido(uuid);

        return ResponseEntity.noContent().build();
    }
}