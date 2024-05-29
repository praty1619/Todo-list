package com.mytodolist.controller;

import com.mytodolist.dto.AuthRequestDTO;
import com.mytodolist.model.User;
import com.mytodolist.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody AuthRequestDTO authRequestDTO) {
        authService.register(authRequestDTO.getUsername(), authRequestDTO.getPassword());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody AuthRequestDTO authRequestDTO) {
        return authService.authenticate(authRequestDTO.getUsername(), authRequestDTO.getPassword())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(401).build());
    }
}
