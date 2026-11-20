package com.shop.model;

import java.math.BigDecimal;

public record Order(Long id, Long userId, BigDecimal total, String status) {}
