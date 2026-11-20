package com.shop.controller;

import com.shop.model.Cart;
import com.shop.model.Order;
import com.shop.service.CouponService;
import com.shop.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final CouponService couponService;

    public OrderController(OrderService orderService, CouponService couponService) {
        this.orderService = orderService;
        this.couponService = couponService;
    }

    @PostMapping
    public Order placeOrder(@RequestHeader("X-User-Id") Long userId,
                            @RequestBody Cart cart,
                            @RequestParam(required = false) String couponCode) {
        return orderService.placeOrder(userId, cart, couponService.findValidCoupon(couponCode));
    }

    @GetMapping("/{id}")
    public Order getOrder(@PathVariable Long id) {
        return orderService.getOrder(id);
    }

    @GetMapping
    public List<Order> myOrders(@RequestHeader("X-User-Id") Long userId) {
        return orderService.getOrdersForUser(userId);
    }

    @PostMapping("/{id}/cancel")
    public Order cancel(@PathVariable Long id) {
        return orderService.cancelOrder(id);
    }
}
