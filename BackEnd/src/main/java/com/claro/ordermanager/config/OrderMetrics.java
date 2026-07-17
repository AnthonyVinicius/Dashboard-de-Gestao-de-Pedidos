package com.claro.ordermanager.config;

import com.claro.ordermanager.entity.OrderStatus;
import com.claro.ordermanager.repository.OrderRepository;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class OrderMetrics {

    public OrderMetrics(
            MeterRegistry meterRegistry,
            OrderRepository orderRepository
    ) {
        Gauge.builder(
                        "pedidos_total",
                        orderRepository,
                        repository -> repository.count()
                )
                .description("Quantidade total de pedidos cadastrados")
                .register(meterRegistry);

        for (OrderStatus status : OrderStatus.values()) {
            Gauge.builder(
                            "pedidos_by_status",
                            orderRepository,
                            repository -> repository.countByStatus(status)
                    )
                    .description("Quantidade de pedidos por status")
                    .tag("status", status.name())
                    .register(meterRegistry);
        }
    }
}