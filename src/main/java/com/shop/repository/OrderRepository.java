package com.shop.repository;

import com.shop.model.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {

    Optional<Order> findById(Long id);

    List<Order> findByUserId(Long userId);

    Order save(Order order);
}
