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


    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registerKitchenStaff(@Valid @RequestBody KitchenStaffDto kitchenStaffDto) {
        try {
            
            AuthResponse authResponse = kitchenStaffService.registerStaff(kitchenStaffDto);

            return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace(); 
            return ResponseEntity.badRequest().build();
        }
    }

}