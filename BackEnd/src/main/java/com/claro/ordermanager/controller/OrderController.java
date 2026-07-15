package com.claro.ordermanager.controller;

import com.claro.ordermanager.dto.OrderRequestDTO;
import com.claro.ordermanager.dto.OrderResponseDTO;
import com.claro.ordermanager.dto.UpdateOrderStatusRequestDTO;
import com.claro.ordermanager.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> getAllOrders() {
        return ResponseEntity.ok(
                orderService.getAllOrders()
        );
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<OrderResponseDTO> getOrderById(
            @PathVariable UUID uuid
    ) {
        return ResponseEntity.ok(
                orderService.getOrderById(uuid)
        );
    }

    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder(
            @Valid @RequestBody OrderRequestDTO request
    ) {
        OrderResponseDTO createdOrder =
                orderService.createOrder(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdOrder);
    }

    @PatchMapping("/{uuid}/status")
    public ResponseEntity<OrderResponseDTO> updateOrderStatus(
            @PathVariable UUID uuid,
            @Valid @RequestBody UpdateOrderStatusRequestDTO request
    ) {
        OrderResponseDTO updatedOrder =
                orderService.updateOrderStatus(uuid, request.status());

        return ResponseEntity.ok(updatedOrder);
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> deleteOrder(
            @PathVariable UUID uuid
    ) {
        orderService.deleteOrder(uuid);
        return ResponseEntity.noContent().build();
    }
}