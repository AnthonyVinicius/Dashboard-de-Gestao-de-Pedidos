package com.claro.pedidos.controller;

import com.claro.pedidos.dto.PedidoRequest;
import com.claro.pedidos.dto.PedidoResponse;
import com.claro.pedidos.service.PedidoService;
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

    private final PedidoService pedidoService;

    @GetMapping
    public ResponseEntity<List<PedidoResponse>> getAllPedidos() {
        return ResponseEntity.ok(
                pedidoService.getAllPedidos()
        );
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<PedidoResponse> getPedidoById(
            @PathVariable UUID uuid
    ) {
        return ResponseEntity.ok(
                pedidoService.getPedidoById(uuid)
        );
    }

    @PostMapping
    public ResponseEntity<PedidoResponse> createPedido(
            @Valid @RequestBody PedidoRequest request
    ) {
        PedidoResponse pedidoCriado =
                pedidoService.createPedido(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(pedidoCriado);
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> deletePedido(
            @PathVariable UUID uuid
    ) {
        pedidoService.deletePedido(uuid);

        return ResponseEntity.noContent().build();
    }
}