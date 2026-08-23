package com.secondserve.server.controller;

import com.secondserve.server.dto.NgoDto;
import com.secondserve.server.service.NgoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController

@RequestMapping("/ngos")
@CrossOrigin(origins = "*", maxAge = 3600)
public class NgoController {

    @Autowired
    private NgoService ngoService;

    
    @PostMapping("/register")
    public ResponseEntity<NgoDto> registerNgo(@Valid @RequestBody NgoDto ngoDto) {
        try {
            NgoDto createdNgo = ngoService.registerNgo(ngoDto);
            return new ResponseEntity<>(createdNgo, HttpStatus.CREATED); // Returns 201 Created on success
        } catch (Exception e) {
            e.printStackTrace(); // Helpful for server-side debugging
            return ResponseEntity.badRequest().build();
        }
    }


    @GetMapping
    public ResponseEntity<List<NgoDto>> getAllNgos() {
        List<NgoDto> ngos = ngoService.getAllActiveNgos();
        return ResponseEntity.ok(ngos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NgoDto> getNgoById(@PathVariable Long id) {
        try {
            NgoDto ngo = ngoService.getNgoById(id);
            return ResponseEntity.ok(ngo);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<NgoDto> updateNgo(@PathVariable Long id, @Valid @RequestBody NgoDto ngoDto) {
        // NOTE: This endpoint should be secured so only the correct NGO can update their own profile.
        try {
            NgoDto updatedNgo = ngoService.updateNgo(id, ngoDto);
            return ResponseEntity.ok(updatedNgo);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNgo(@PathVariable Long id) {
        // NOTE: This endpoint should likely be restricted to administrators.
        try {
            ngoService.deleteNgo(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}