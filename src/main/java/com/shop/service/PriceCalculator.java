package com.shop.service;

import com.shop.model.Cart;
import com.shop.model.CartItem;
import com.shop.model.Coupon;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Calculates prices for a cart: item subtotal, coupon discount and tax.
 */
@Service
public class PriceCalculator {

    private static final BigDecimal TAX_RATE = new BigDecimal("0.18");

    public BigDecimal calculateSubtotal(Cart cart) {
        BigDecimal subtotal = BigDecimal.ZERO;
        for (CartItem item : cart.items()) {
            subtotal = subtotal.add(item.price().multiply(BigDecimal.valueOf(item.quantity())));
        }
        return subtotal;
    }

    public BigDecimal applyDiscount(BigDecimal amount, Coupon coupon) {
        if (coupon == null || coupon.expired()) {
            return amount;
        }
        BigDecimal discount = amount.multiply(BigDecimal.valueOf(coupon.percentOff()))
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        return amount.subtract(discount);
    }

    public BigDecimal calculateTax(BigDecimal amount) {
        return amount.multiply(TAX_RATE).setScale(2, RoundingMode.HALF_UP);
    }

    /** Tax is only charged for orders shipped inside India ("IN"). */
    public BigDecimal calculateTotal(Cart cart, Coupon coupon, String region) {
        BigDecimal afterDiscount = applyDiscount(calculateSubtotal(cart), coupon);
        BigDecimal tax = region.equals("IN") ? calculateTax(afterDiscount) : BigDecimal.ZERO;
        return afterDiscount.add(tax);
    }
}
