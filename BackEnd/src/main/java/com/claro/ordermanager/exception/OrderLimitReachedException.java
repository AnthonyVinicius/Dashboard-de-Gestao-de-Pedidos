package com.claro.ordermanager.exception;

public class OrderLimitReachedException extends RuntimeException {

    public OrderLimitReachedException(String message) {
        super(message);
    }
}

