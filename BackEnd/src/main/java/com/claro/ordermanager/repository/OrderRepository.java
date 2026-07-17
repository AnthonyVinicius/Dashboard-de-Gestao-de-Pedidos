package com.claro.ordermanager.repository;

import com.claro.ordermanager.entity.Order;
import com.claro.ordermanager.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {

    long countByStatus(OrderStatus status);
}