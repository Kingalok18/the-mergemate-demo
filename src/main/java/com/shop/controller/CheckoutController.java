package com.shop.controller;

import com.shop.model.Cart;
import com.shop.model.Coupon;
import com.shop.service.CouponService;
import com.shop.service.InventoryService;
import com.shop.service.PriceCalculator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Lets the customer preview the final price before placing an order.
 */
@RestController
@RequestMapping("/checkout")
public class CheckoutController {

    private final PriceCalculator priceCalculator;
    private final CouponService couponService;
    private final InventoryService inventoryService;

    public CheckoutController(PriceCalculator priceCalculator,
                              CouponService couponService,
                              InventoryService inventoryService) {
        this.priceCalculator = priceCalculator;
        this.couponService = couponService;
        this.inventoryService = inventoryService;
    }

    /** Shows subtotal, discount, tax and total for the cart. */
    @PostMapping("/preview")
    public ResponseEntity<Map<String, Object>> preview(@RequestBody Cart cart,
                                                       @RequestParam(required = false) String couponCode) {
        if (cart.items().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Cart is empty"));
        }
        for (var item : cart.items()) {
            if (!inventoryService.isInStock(item.productId(), item.quantity())) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", item.name() + " is out of stock"));
            }
        }

        Coupon coupon = couponService.findValidCoupon(couponCode);
        BigDecimal subtotal = priceCalculator.calculateSubtotal(cart);
        BigDecimal total = priceCalculator.calculateTotal(cart, coupon);

        return ResponseEntity.ok(Map.of(
                "subtotal", subtotal,
                "coupon", coupon == null ? "none" : coupon.code(),
                "total", total));
    }
}
