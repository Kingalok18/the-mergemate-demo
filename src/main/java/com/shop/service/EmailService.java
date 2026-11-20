package com.shop.service;

import com.shop.model.Order;
import com.shop.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Sends emails to customers. (Simulated: writes them to the log.)
 */
@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    public void sendWelcome(User user) {
        send(user.email(), "Welcome to the shop", "Hi " + user.name() + ", thanks for joining!");
    }

    public void sendOrderConfirmation(User user, Order order) {
        String body = "Hi " + user.name() + ", your order #" + order.id()
                + " for Rs. " + order.total() + " is confirmed.";
        send(user.email(), "Order confirmed", body);
    }

    private void send(String to, String subject, String body) {
        log.info("Email to {} | {} | {}", to, subject, body);
    }
}
