package com.shop.service;

import com.shop.model.User;
import com.shop.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email.trim().toLowerCase());
    }

    public User createUser(String name, String email, String passwordHash) {
        return userRepository.save(new User(null, name, email.trim().toLowerCase(), passwordHash));
    }

    public User updateName(Long id, String newName) {
        User user = getUserById(id);
        return userRepository.save(new User(user.id(), newName, user.email(), user.passwordHash()));
    }
}
