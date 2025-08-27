package com.example.demo.Controllers;

import com.example.demo.DTOs.UserRequest;
import com.example.demo.DTOs.UserResponse;
import com.example.demo.entity.AuthRequest;
import com.example.demo.entity.AuthResponse;
import com.example.demo.entity.User;
import com.example.demo.Repositories.UserRepository;
import com.example.demo.Security.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Tag(name = "Authentication", description = "Authentication Management APIs")
@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Operation(summary = "Log In", description = "Log In into a pre-registered account")
    @PostMapping("/login")
    public ResponseEntity<?> generateToken(@Valid @RequestBody AuthRequest request) {
        try {
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body(new AuthResponse("Invalid credentials"));
        }

        String token = jwtUtil.generateToken(request.getUsername());
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @Operation(summary = "Register a new account", description = "Add a new account to the system")
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRequest userRequest) {
        if (userRepository.findByUsername(userRequest.getUsername()) != null) {
            return ResponseEntity.badRequest().body("Username already taken!");
        }

        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setRole(userRequest.getRole());

        User savedUser = userRepository.save(user);
        UserResponse response = new UserResponse(savedUser.getId(), savedUser.getUsername(), savedUser.getRole());

        return ResponseEntity.ok(response);
    }


}
