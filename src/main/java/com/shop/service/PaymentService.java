package com.shop.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Talks to the payment gateway. (Simulated: always succeeds for positive amounts.)
 */
@Service
public class PaymentService {

    private final String gatewayUrl;

    public PaymentService(@Value("${payment.gateway-url}") String gatewayUrl) {
        this.gatewayUrl = gatewayUrl;
    }

    /** Charges the user and returns a payment id, or null if the charge failed. */
    public String charge(Long userId, BigDecimal amount) {
        if (amount == null || amount.signum() <= 0) {
            return null;
        }
        return "pay_" + UUID.randomUUID();
    }

    public boolean refund(Long userId, BigDecimal amount) {
        return amount != null && amount.signum() > 0;
    }

    public String getGatewayUrl() {
        return gatewayUrl;
    }
}
