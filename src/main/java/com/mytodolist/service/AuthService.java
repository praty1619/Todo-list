package com.mytodolist.service;

import com.mytodolist.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class AuthService {
    private final List<User> users = new ArrayList<>();
    private final AtomicLong userIdSequence = new AtomicLong(1);

    public void register(String username, String password) {
        if (users.stream().anyMatch(user -> user.getUsername().equals(username))) {
            throw new RuntimeException("Username is already taken");
        }

        User user = new User();
        user.setId(userIdSequence.getAndIncrement());
        user.setUsername(username);
        user.setPassword(password); // Passwords should be hashed in a real application
        users.add(user);
    }

    public Optional<User> findByUsername(String username) {
        return users.stream().filter(user -> user.getUsername().equals(username)).findFirst();
    }

    public Optional<User> authenticate(String username, String password) {
        return users.stream()
                .filter(user -> user.getUsername().equals(username) && user.getPassword().equals(password))
                .findFirst();
    }
}
