package com.shop.service;

import com.shop.model.Coupon;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Looks up discount coupons by code.
 */
@Service
public class CouponService {

    private final Map<String, Coupon> coupons = Map.of(
            "WELCOME10", new Coupon("WELCOME10", 10, false),
            "FESTIVE25", new Coupon("FESTIVE25", 25, false),
            "OLD50", new Coupon("OLD50", 50, true));

    /** Returns the coupon if the code exists and is not expired, otherwise null. */
    public Coupon findValidCoupon(String code) {
        if (code == null || code.isBlank()) {
            return null;
        }
        Coupon coupon = coupons.get(code.trim().toUpperCase());
        if (coupon == null || coupon.expired()) {
            return null;
        }
        return coupon;
    }
}
