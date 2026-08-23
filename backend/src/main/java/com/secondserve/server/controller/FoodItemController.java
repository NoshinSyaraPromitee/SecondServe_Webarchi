package com.secondserve.server.controller;

import com.secondserve.server.dto.FoodItemDto;
import com.secondserve.server.service.FoodItemService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.secondserve.server.repository.NgoRepository;
import com.secondserve.server.repository.HotelRepository;
import com.secondserve.server.repository.KitchenStaffRepository;
import com.secondserve.server.entity.KitchenStaff;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

@RestController
@RequestMapping("/food-items")
@CrossOrigin(origins = "*", maxAge = 3600)
public class FoodItemController {

    @Autowired
    private FoodItemService foodItemService;
    @Autowired
    private NgoRepository ngoRepository;
    @Autowired
    private HotelRepository hotelRepository;
    @Autowired
    private KitchenStaffRepository kitchenStaffRepository;

    @GetMapping("/available")
    public ResponseEntity<List<FoodItemDto>> getAvailableFoodItems(Authentication authentication) {
        // If an NGO is logged in (this endpoint is public, so authentication may
        // be null), tag each item with that NGO's own request status so the
        // frontend can show "Requested / Approved" instead of always offering
        // "Request this food" again for items they've already requested.
        Long ngoId = null;
        if (authentication != null) {
            ngoId = ngoRepository.findByEmail(authentication.getName())
                    .map(ngo -> ngo.getId())
                    .orElse(null);
        }

        List<FoodItemDto> foodItems = foodItemService.getAllAvailableFoodItems(ngoId);
        return ResponseEntity.ok(foodItems);
    }

    @GetMapping("/hotel/{hotelId}")
    public ResponseEntity<List<FoodItemDto>> getFoodItemsByHotel(@PathVariable Long hotelId, Authentication authentication) {
        String email = authentication.getName();

        Long userId = ngoRepository.findByEmail(email)
                .map(ngo -> ngo.getId())
                .orElseGet(() -> hotelRepository.findByEmail(email)
                        .map(hotel -> hotel.getId())
                        .orElseThrow(() -> new UsernameNotFoundException("Authenticated user not found in database: " + email))
                );

        List<FoodItemDto> foodItems = foodItemService.getFoodItemsByHotel(hotelId, userId);
        return ResponseEntity.ok(foodItems);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FoodItemDto> getFoodItemById(@PathVariable Long id) {
        try {
            FoodItemDto foodItem = foodItemService.getFoodItemById(id);
            return ResponseEntity.ok(foodItem);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // --- THE FIX IS HERE ---
    @PostMapping
    public ResponseEntity<FoodItemDto> createFoodItem(@Valid @RequestBody FoodItemDto foodItemDto, Authentication authentication) {
        try {
            // Resolve the hotel from the currently logged-in kitchen staff member,
            // instead of assuming hotel ID 1. Previously this was hardcoded, so
            // every kitchen staff account's entries were silently filed under
            // hotel #1 and never showed up in their own hotel manager's
            // "pending review" list (and therefore never reached the NGO side).
            String email = authentication.getName();
            KitchenStaff staff = kitchenStaffRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("Authenticated kitchen staff not found: " + email));

            Long hotelIdOfLoggedInUser = staff.getHotel().getId();
            FoodItemDto createdFoodItem = foodItemService.createFoodItem(foodItemDto, hotelIdOfLoggedInUser);
            return new ResponseEntity<>(createdFoodItem, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    // --- NEW ENDPOINT for the surplus food log ---
    @GetMapping("/my-log")
    public ResponseEntity<List<FoodItemDto>> getMyFoodLog(Authentication authentication) {
        String email = authentication.getName();
        KitchenStaff staff = kitchenStaffRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Authenticated kitchen staff not found: " + email));

        List<FoodItemDto> log = foodItemService.getFoodLogForHotel(staff.getHotel().getId());
        return ResponseEntity.ok(log);
    }

    // --- NEW: full surplus food log, for the Hotel manager dashboard ---
    // Same underlying data as /my-log, but scoped by hotelId from the path
    // instead of resolving hotel from a KitchenStaff account, so a logged-in
    // Hotel manager can view it too.
    @GetMapping("/hotel/{hotelId}/log")
    public ResponseEntity<List<FoodItemDto>> getFoodLogByHotel(@PathVariable Long hotelId) {
        List<FoodItemDto> log = foodItemService.getFoodLogForHotel(hotelId);
        return ResponseEntity.ok(log);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FoodItemDto> updateFoodItem(@PathVariable Long id, @Valid @RequestBody FoodItemDto foodItemDto) {
        try {
            FoodItemDto updatedFoodItem = foodItemService.updateFoodItem(id, foodItemDto);
            return ResponseEntity.ok(updatedFoodItem);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/unavailable")
    public ResponseEntity<Void> markAsUnavailable(@PathVariable Long id) {
        try {
            foodItemService.markAsUnavailable(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFoodItem(@PathVariable Long id) {
        try {
            foodItemService.deleteFoodItem(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<Void> approveFoodItem(@PathVariable Long id) {
        try {
            foodItemService.markAsAvailable(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/hotel/{hotelId}/pending")
    public ResponseEntity<List<FoodItemDto>> getPendingFoodItemsByHotel(@PathVariable Long hotelId) {
        List<FoodItemDto> foodItems = foodItemService.getPendingFoodItemsByHotel(hotelId);
        return ResponseEntity.ok(foodItems);
    }

    @GetMapping("/hotel/{hotelId}/today")
    public ResponseEntity<List<FoodItemDto>> getTodaysFoodItemsByHotel(@PathVariable Long hotelId) {
        List<FoodItemDto> foodItems = foodItemService.getTodaysFoodItemsByHotel(hotelId);
        return ResponseEntity.ok(foodItems);
    }
}