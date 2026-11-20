package com.shop.model;

public record User(Long id, String name, String email, String passwordHash) {}
