package com.claro.ordermanager.config;

import com.claro.ordermanager.entity.Order;
import com.claro.ordermanager.entity.OrderStatus;
import com.claro.ordermanager.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final OrderRepository orderRepository;

    @Override
    public void run(String... args) {

        if (orderRepository.count() > 0) {
            return;
        }
        Order order1 = new Order();

        order1.setDisplayName("Pedido #1 - João Silva");
        order1.setItems(2);
        order1.setWeight(1024);
        order1.setStatus(OrderStatus.EM_PROCESSAMENTO);
        orderRepository.save(order1);

        Order order2 = new Order();
        order2.setDisplayName("Pedido #2 - Maria Souza");
        order2.setItems(1);
        order2.setWeight(512);
        order2.setStatus(OrderStatus.PAUSADO);
        orderRepository.save(order2);

        Order order3 = new Order();
        order3.setDisplayName("Pedido #3 - Carlos Lima");
        order3.setItems(4);
        order3.setWeight(2048);
        order3.setStatus(OrderStatus.CANCELADO);
        orderRepository.save(order3);
    }
}
