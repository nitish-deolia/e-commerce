package com.example.authservice.service;

import com.example.authservice.model.User;
import com.example.authservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.security.Key;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    private Key key = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);

    public User register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public String login(String email, String password) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        if (passwordEncoder.matches(password, user.getPassword())) {
            return Jwts.builder().setSubject(user.getId().toString()).signWith(key).compact();
        }
        throw new RuntimeException("Invalid credentials");
    }
}