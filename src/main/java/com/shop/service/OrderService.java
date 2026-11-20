package com.shop.service;

import com.shop.model.Cart;
import com.shop.model.Coupon;
import com.shop.model.Order;
import com.shop.model.User;
import com.shop.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * Places orders: checks stock, calculates the price, takes payment
 * and sends a confirmation email.
 */
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final PriceCalculator priceCalculator;
    private final InventoryService inventoryService;
    private final PaymentService paymentService;
    private final EmailService emailService;
    private final UserService userService;

    public OrderService(OrderRepository orderRepository, PriceCalculator priceCalculator,
                        InventoryService inventoryService, PaymentService paymentService,
                        EmailService emailService, UserService userService) {
        this.orderRepository = orderRepository;
        this.priceCalculator = priceCalculator;
        this.inventoryService = inventoryService;
        this.paymentService = paymentService;
        this.emailService = emailService;
        this.userService = userService;
    }

    public Order placeOrder(Long userId, Cart cart, Coupon coupon) {
        User user = userService.getUserById(userId);
        inventoryService.reserve(cart.items());

        BigDecimal total = priceCalculator.calculateTotal(cart, coupon);
        String paymentId = paymentService.charge(user.id(), total);
        if (paymentId == null) {
            inventoryService.release(cart.items());
            throw new IllegalStateException("Payment failed for user " + userId);
        }

        Order order = orderRepository.save(new Order(null, user.id(), total, "PAID"));
        emailService.sendOrderConfirmation(user, order);
        return order;
    }

    public Order getOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId));
    }

    public List<Order> getOrdersForUser(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    public Order cancelOrder(Long orderId) {
        Order order = getOrder(orderId);
        if (!"PAID".equals(order.status())) {
            throw new IllegalStateException("Only paid orders can be cancelled");
        }
        paymentService.refund(order.userId(), order.total());
        return orderRepository.save(new Order(order.id(), order.userId(), order.total(), "CANCELLED"));
    }
}
