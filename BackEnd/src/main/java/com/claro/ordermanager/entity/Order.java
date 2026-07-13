package com.claro.ordermanager.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String displayName;

    @Column(nullable = false)
    private Integer items;

    @Column(nullable = false)
    private Integer weight;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;
}