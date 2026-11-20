package com.shop.service;

import com.shop.model.Cart;
import com.shop.model.CartItem;
import com.shop.model.Coupon;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PriceCalculatorTest {

    private final PriceCalculator calculator = new PriceCalculator();

    private Cart cartOf(String price, int quantity) {
        return new Cart(1L, List.of(new CartItem("BOOK-1", "Book", new BigDecimal(price), quantity)));
    }

    @Test
    void totalWithoutCouponAddsTax() {
        assertEquals(new BigDecimal("236.00"), calculator.calculateTotal(cartOf("100.00", 2), null, "IN"));
    }

    @Test
    void couponIsAppliedBeforeTax() {
        Coupon tenPercent = new Coupon("WELCOME10", 10, false);
        assertEquals(new BigDecimal("212.40"), calculator.calculateTotal(cartOf("100.00", 2), tenPercent, "IN"));
    }

    @Test
    void expiredCouponIsIgnored() {
        Coupon expired = new Coupon("OLD50", 50, true);
        assertEquals(new BigDecimal("100.00"), calculator.applyDiscount(new BigDecimal("100.00"), expired));
    }
}
