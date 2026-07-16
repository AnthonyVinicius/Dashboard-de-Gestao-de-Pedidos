package com.claro.Orders.service;

import com.claro.Orders.dto.OrderRequest;
import com.claro.Orders.dto.OrderResponse;
import com.claro.Orders.entity.Order;
import com.claro.Orders.entity.StatusOrder;
import com.claro.Orders.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private static final int LIMITE_MAXIMO_OrderS = 5;

    private final OrderRepository OrderRepository;

    public List<OrderResponse> getAllOrders() {
        return OrderRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public OrderResponse getOrderById(UUID OrderUUID) {
        Order Order = findOrderById(OrderUUID);

        return toResponse(Order);
    }

    public OrderResponse createOrder(OrderRequest request) {
        if (OrderRepository.count() >= LIMITE_MAXIMO_OrderS) {
            throw new RuntimeException(
                    "O limite máximo de 5 Orders foi atingido"
            );
        }

        Order Order = new Order();

        Order.setDisplayName(request.displayName());
        Order.setItens(request.itens());
        Order.setPeso(request.peso());
        Order.setStatus(StatusOrder.EM_PROCESSAMENTO);

        Order OrderSalvo = OrderRepository.save(Order);

        return toResponse(OrderSalvo);
    }

    public void deleteOrder(UUID OrderUUID) {
        Order Order = findOrderById(OrderUUID);

        OrderRepository.delete(Order);
    }

    private Order findOrderById(UUID OrderUUID) {
        return OrderRepository.findById(OrderUUID)
                .orElseThrow(() ->
                        new RuntimeException("Order não encontrado")
                );
    }

    private OrderResponse toResponse(Order Order) {
        return new OrderResponse(
                order.getId(),
                order.getDisplayName(),
                order.getItens(),
                order.getPeso(),
                order.getStatus()
        );
    }
}