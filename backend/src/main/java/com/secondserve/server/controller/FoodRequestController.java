package com.secondserve.server.controller;

import com.secondserve.server.dto.FoodRequestDto;
import com.secondserve.server.entity.FoodRequest.RequestStatus;
import com.secondserve.server.repository.NgoRepository;
import com.secondserve.server.service.FoodRequestService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/food-requests")
@CrossOrigin(origins = "*", maxAge = 3600)
public class FoodRequestController {

    @Autowired
    private FoodRequestService foodRequestService;
    @Autowired
    private NgoRepository ngoRepository;

    @PostMapping
    public ResponseEntity<FoodRequestDto> createFoodRequest(@Valid @RequestBody FoodRequestDto foodRequestDto, Authentication authentication) {
        try {
            // Resolve the NGO from the currently logged-in user's token instead of
            // assuming NGO ID 1. Previously this was hardcoded, so every NGO's
            // request was silently filed under NGO #1 — the real NGO's "My
            // requests" list (queried by their own ID) never showed it, so it
            // looked like the request vanished and no update ever arrived.
            String email = authentication.getName();
            Long loggedInNgoId = ngoRepository.findByEmail(email)
                    .map(ngo -> ngo.getId())
                    .orElseThrow(() -> new UsernameNotFoundException("Authenticated NGO not found: " + email));

            FoodRequestDto createdRequest = foodRequestService.createFoodRequest(foodRequestDto, loggedInNgoId);
            return new ResponseEntity<>(createdRequest, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<FoodRequestDto> approveRequest(@PathVariable Long id) {
        try {
            FoodRequestDto updatedRequest = foodRequestService.updateRequestStatus(id, RequestStatus.APPROVED);
            return ResponseEntity.ok(updatedRequest);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<FoodRequestDto> rejectRequest(@PathVariable Long id) {
        try {
            FoodRequestDto updatedRequest = foodRequestService.updateRequestStatus(id, RequestStatus.REJECTED);
            return ResponseEntity.ok(updatedRequest);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<Void> completeRequest(@PathVariable Long id) {  // FIXED: Changed "Id" to "id"
        try {
            System.out.println("=== Completing request with ID: " + id + " ==="); // Debug log
            foodRequestService.completeFoodRequest(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace(); // This will now show the actual error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/hotel/{hotelId}")
    public ResponseEntity<?> getRequestsForHotel(
            @PathVariable Long hotelId,
            @RequestParam(name = "status", required = false) String statusStr) {

        if (statusStr != null && !statusStr.isBlank()) {
            try {
                // This manually converts the string to an enum, making it case-insensitive and safe
                RequestStatus status = RequestStatus.valueOf(statusStr.toUpperCase());
                List<FoodRequestDto> requests = foodRequestService.getRequestsByHotelAndStatus(hotelId, status);
                return ResponseEntity.ok(requests);
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body("Invalid status value: " + statusStr);
            }
        } else {
            // If no status is provided, get all requests
            List<FoodRequestDto> requests = foodRequestService.getRequestsForHotel(hotelId);
            return ResponseEntity.ok(requests);
        }
    }

    @GetMapping("/ngo/{ngoId}")
    public ResponseEntity<List<FoodRequestDto>> getRequestsByNgo(@PathVariable Long ngoId) {
        System.out.println("=== Getting requests for NGO: " + ngoId + " ==="); // Debug
        try {
            List<FoodRequestDto> requests = foodRequestService.getRequestsByNgo(ngoId);
            System.out.println("Found " + requests.size() + " requests"); // Debug
            return ResponseEntity.ok(requests);
        } catch (Exception e) {
            e.printStackTrace(); // This will show the full stack trace
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}