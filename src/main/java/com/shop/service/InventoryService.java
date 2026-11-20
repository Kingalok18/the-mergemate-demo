package com.shop.service;

import com.shop.model.CartItem;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Keeps track of how many units of each product are in stock.
 */
@Service
public class InventoryService {

    private final Map<String, Integer> stock = new ConcurrentHashMap<>(Map.of(
            "BOOK-1", 20,
            "PEN-2", 100,
            "BAG-3", 5));

    public boolean isInStock(String productId, int quantity) {
        return stock.getOrDefault(productId, 0) >= quantity;
    }

    public void reserve(List<CartItem> items) {
        for (CartItem item : items) {
            if (!isInStock(item.productId(), item.quantity())) {
                throw new IllegalStateException(item.name() + " is out of stock");
            }
        }
        for (CartItem item : items) {
            stock.merge(item.productId(), -item.quantity(), Integer::sum);
        }
    }

    public void release(List<CartItem> items) {
        for (CartItem item : items) {
            stock.merge(item.productId(), item.quantity(), Integer::sum);
        }
    }
}
