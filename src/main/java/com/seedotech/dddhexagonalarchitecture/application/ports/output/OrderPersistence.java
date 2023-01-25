package com.seedotech.dddhexagonalarchitecture.application.ports.output;

import com.seedotech.dddhexagonalarchitecture.domain.Order;

import java.util.Optional;

public interface OrderPersistence {

    Order saveOrder(Order order);

    Optional<Order> getOrderById(Long id);
}
