package com.expensetracker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.expensetracker.entity.User;
import com.expensetracker.service.UserService;
import com.expensetracker.security.JwtService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:5173")   // allow React frontend
public class AuthController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService service;

    // Register User
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {

        User newUser = service.register(user);

        return ResponseEntity.ok(newUser);
    }

    // Login User
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {

        User u = service.login(user.getEmail(), user.getPassword());

        if (u != null) {

            String token = jwtService.generateToken(u.getEmail());

            Map<String, String> response = new HashMap<>();
            response.put("token", token);

            return ResponseEntity.ok(response);
        }

        Map<String, String> error = new HashMap<>();
        error.put("message", "Invalid email or password");

        return ResponseEntity.status(401).body(error);
    }
}