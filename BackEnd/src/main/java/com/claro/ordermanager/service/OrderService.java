package com.claro.ordermanager.service;

import com.claro.ordermanager.dto.OrderRequestDTO;
import com.claro.ordermanager.dto.OrderResponseDTO;
import com.claro.ordermanager.entity.Order;
import com.claro.ordermanager.entity.OrderStatus;
import com.claro.ordermanager.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private static final int ORDER_LIMIT = 5;

    private final OrderRepository orderRepository;

    @Transactional(readOnly = true)
    public List<OrderResponseDTO> getAllOrders() {

        log.info("Listing all orders.");

        return orderRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public OrderResponseDTO getOrderById(UUID orderUUID) {

        log.info("Searching order with id {}.", orderUUID);

        Order order = findOrderById(orderUUID);

        return toResponse(order);
    }

    @Transactional
    public OrderResponseDTO createOrder(OrderRequestDTO request) {

        log.info("Creating order for '{}'.", request.displayName());

        if (orderRepository.count() >= ORDER_LIMIT) {

            log.warn("Order creation denied. Maximum limit of {} orders reached.", ORDER_LIMIT);

            throw new RuntimeException(
                    "The maximum limit of " + ORDER_LIMIT + " orders has been reached."
            );
        }

        Order order = new Order();

        order.setDisplayName(request.displayName());
        order.setItems(request.items());
        order.setWeight(request.weight());
        order.setStatus(OrderStatus.PROCESSING);

        Order savedOrder = orderRepository.save(order);

        log.info("Order created successfully. ID={}", savedOrder.getId());

        return toResponse(savedOrder);
    }

    @Transactional
    public OrderResponseDTO updateOrderStatus(
            UUID orderUUID,
            OrderStatus status
    ) {

        log.info("Updating order {} to status {}.", orderUUID, status);

        Order order = findOrderById(orderUUID);

        order.setStatus(status);

        Order updatedOrder = orderRepository.save(order);

        log.info("Order {} updated successfully.", updatedOrder.getId());

        return toResponse(updatedOrder);
    }

    @Transactional
    public void deleteOrder(UUID orderUUID) {

        log.info("Deleting order {}.", orderUUID);

        Order order = findOrderById(orderUUID);

        orderRepository.delete(order);

        log.info("Order {} deleted successfully.", orderUUID);
    }

    private Order findOrderById(UUID orderUUID) {

        return orderRepository.findById(orderUUID)
                .orElseThrow(() -> {

                    log.error("Order {} not found.", orderUUID);

                    return new RuntimeException("Order not found");
                });
    }

    private OrderResponseDTO toResponse(Order order) {
        return new OrderResponseDTO(
                order.getId(),
                order.getDisplayName(),
                order.getItems(),
                order.getWeight(),
                order.getStatus()
        );
    }
}