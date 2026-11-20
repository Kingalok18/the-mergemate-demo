package com.shop.service;

import com.shop.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Handles user login, registration and logout.
 * A successful login returns a random session token.
 */
@Service
public class AuthService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final Map<String, Long> sessions = new ConcurrentHashMap<>();

    public AuthService(UserService userService, PasswordEncoder passwordEncoder, EmailService emailService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    public String login(String email, String password) {
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));
        if (!passwordEncoder.matches(password, user.passwordHash())) {
            throw new IllegalArgumentException("Invalid email or password");
        }
        String token = UUID.randomUUID().toString();
        sessions.put(token, user.id());
        return token;
    }

    public User register(String name, String email, String password) {
        if (userService.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email already registered");
        }
        User user = userService.createUser(name, email, passwordEncoder.encode(password));
        emailService.sendWelcome(user);
        return user;
    }

    public void logout(String token) {
        sessions.remove(token);
    }

    public Long getUserIdForToken(String token) {
        return sessions.get(token);
    }
}
