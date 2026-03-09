package com.expensetracker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.expensetracker.entity.User;
import com.expensetracker.service.UserService;
import com.expensetracker.security.JwtService;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService service;

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {

        User newUser = service.register(user);

        return ResponseEntity.ok(newUser);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {

        User u = service.login(user.getEmail(), user.getPassword());

        if (u != null) {

            String token = jwtService.generateToken(u.getEmail());

            return ResponseEntity.ok(token);
        }

        return ResponseEntity.status(401).body("Invalid email or password");
    }
}