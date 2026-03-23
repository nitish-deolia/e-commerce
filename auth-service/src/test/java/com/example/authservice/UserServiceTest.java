package com.example.authservice;

import com.example.authservice.model.User;
import com.example.authservice.repository.UserRepository;
import com.example.authservice.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    public void testRegister() {
        User user = new User();
        user.setUsername("test");
        user.setEmail("test@example.com");
        user.setPassword("password");

        when(passwordEncoder.encode("password")).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.register(user);

        assertNotNull(result);
        verify(passwordEncoder).encode("password");
        verify(userRepository).save(user);
    }

    @Test
    public void testLoginSuccess() {
        User user = new User();
        user.setUserId(UUID.randomUUID());
        user.setEmail("test@example.com");
        user.setPasswordHash("encoded");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password", "encoded")).thenReturn(true);

        String token = userService.login("test@example.com", "password");

        assertNotNull(token);
    }

    @Test
    public void testLoginFailure() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.login("test@example.com", "password"));
    }
}
