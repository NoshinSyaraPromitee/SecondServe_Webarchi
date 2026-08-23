package com.secondserve.server.controller;

import com.secondserve.server.dto.AuthResponse;
import com.secondserve.server.dto.KitchenStaffDto;
import com.secondserve.server.service.KitchenStaffService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController

@RequestMapping("/staff")
@CrossOrigin(origins = "*", maxAge = 3600)
public class KitchenStaffController {

    @Autowired
    private KitchenStaffService kitchenStaffService;

    /**
     * Handles the registration of a new Kitchen Staff member.
     * This is the specific endpoint that the "Register as Kitchen Staff" form will call.
     *
     * @param kitchenStaffDto The user's registration details, sent as a JSON object in the request body.
     * @return On success, an AuthResponse containing a JWT token for immediate login.
     *         On failure, a Bad Request response.
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registerKitchenStaff(@Valid @RequestBody KitchenStaffDto kitchenStaffDto) {
        try {
            
            AuthResponse authResponse = kitchenStaffService.registerStaff(kitchenStaffDto);

            // Return HTTP Status 201 Created, which is the correct standard for a successful registration.
            return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
        } catch (Exception e) {
            // If the service throws an error (e.g., duplicate email, invalid hotel code),
            // log the error and return a 400 Bad Request status.
            e.printStackTrace(); 
            return ResponseEntity.badRequest().build();
        }
    }

}