package com.example.authservice.controller;

import com.example.authservice.model.User;
import com.example.authservice.service.UserService;
import com.example.authservice.dto.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public User register(@Valid @RequestBody User user) {
        return userService.register(user);
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request) {
        return userService.login(request.getEmail(), request.getPassword());
    }
}