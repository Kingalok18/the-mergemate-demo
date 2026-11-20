package com.shop.service;

import com.shop.model.User;
import com.shop.repository.UserRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserServiceTest {

    private final UserRepository userRepository = mock(UserRepository.class);
    private final UserService userService = new UserService(userRepository);

    @Test
    void getUserByIdReturnsUser() {
        User user = new User(1L, "Asha", "asha@example.com", "hash");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        assertEquals("Asha", userService.getUserById(1L).name());
    }

    @Test
    void getUserByIdThrowsWhenMissing() {
        when(userRepository.findById(9L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> userService.getUserById(9L));
    }
}
