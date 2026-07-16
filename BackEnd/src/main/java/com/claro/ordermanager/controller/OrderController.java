package com.claro.ordermanager.controller;

import com.claro.ordermanager.dto.OrderRequest;
import com.claro.ordermanager.dto.OrderResponse;
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
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        return ResponseEntity.ok(
                orderService.getAllOrders()
        );
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<OrderResponse> getOrderById(
            @PathVariable UUID uuid
    ) {
        return ResponseEntity.ok(
                orderService.getOrderById(uuid)
        );
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
            @Valid @RequestBody OrderRequest request
    ) {
        OrderResponse createdOrder =
                orderService.createOrder(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdOrder);
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> deleteOrder(
            @PathVariable UUID uuid
    ) {
        orderService.deleteOrder(uuid);
        return ResponseEntity.noContent().build();
    }
}