package com.claro.ordermanager.service;

import com.claro.ordermanager.dto.OrderRequest;
import com.claro.ordermanager.dto.OrderResponse;
import com.claro.ordermanager.entity.Order;
import com.claro.ordermanager.entity.OrderStatus;
import com.claro.ordermanager.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private static final int ORDER_LIMIT = 5;

    private final OrderRepository OrderRepository;

    public List<OrderResponse> getAllOrders() {
        return OrderRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public OrderResponse getOrderById(UUID orderUUID) {
        Order order = findOrderById(orderUUID);
        return toResponse(order);
    }

    public OrderResponse createOrder(OrderRequest request) {
        if (OrderRepository.count() >= ORDER_LIMIT) {
            throw new RuntimeException(
                    "The maximum limit of 5 orders has been reached."
            );
        }

        Order order = new Order();

        order.setDisplayName(request.displayName());
        order.setItems(request.items());
        order.setWeight(request.weight());
        order.setStatus(OrderStatus.PROCESSING);

        Order orderSalvo = OrderRepository.save(order);

        return toResponse(orderSalvo);
    }

    public void deleteOrder(UUID orderUUID) {
        Order order = findOrderById(orderUUID);

        OrderRepository.delete(order);
    }

    private Order findOrderById(UUID orderUUID) {
        return OrderRepository.findById(orderUUID)
                .orElseThrow(() ->
                        new RuntimeException("Order not found")
                );
    }

    private OrderResponse toResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getDisplayName(),
                order.getItems(),
                order.getWeight(),
                order.getStatus()
        );
    }
}