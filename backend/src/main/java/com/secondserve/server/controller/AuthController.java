package com.secondserve.server.controller;

import com.secondserve.server.dto.AuthResponse;
import com.secondserve.server.dto.LoginRequest;
import com.secondserve.server.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController

@RequestMapping("/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    @Autowired
    private AuthService authService;


    /**
     * Authenticates a user (Hotel Manager, Kitchen Staff, or NGO) and returns a JWT.
     * @param loginRequest The user's email, password, and userType.
     * @return On success, an AuthResponse containing the JWT token and user details.
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            AuthResponse authResponse = authService.login(loginRequest);
            return ResponseEntity.ok(authResponse);
        } catch (Exception e) {
           
            e.printStackTrace(); // Helpful for debugging
            
            return ResponseEntity.status(401).build();
        }
    }
}