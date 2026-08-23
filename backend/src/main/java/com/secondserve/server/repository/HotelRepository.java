package com.secondserve.server.repository;

import com.secondserve.server.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {

    // --- METHODS FOR AUTHENTICATION & REGISTRATION ---

    // Used by AuthService to find a user during the login process.
    Optional<Hotel> findByEmail(String email);

    // Used by HotelService to check for duplicate emails during registration.
    boolean existsByEmail(String email);


    // --- METHODS ADDED TO SUPPORT THE HOTEL & KITCHEN STAFF WORKFLOW ---

    /**
     * Finds a hotel by its unique, shareable code.
     * This is used during Kitchen Staff registration to link them to the correct hotel.
     */
    Optional<Hotel> findByHotelCode(String hotelCode);

    /**
     * Quickly checks if a hotel_code already exists in the database.
     * This is used by the HotelService to guarantee that newly generated codes are unique.
     */
    boolean existsByHotelCode(String hotelCode);


    // --- METHOD FOR BROWSING ---

    // Used by NGOs to get a list of all active hotels they can potentially receive donations from.
    List<Hotel> findByIsActiveTrue();

    @Query("SELECT h FROM Hotel h WHERE h.isActive = true AND EXISTS " +
            "(SELECT 1 FROM FoodItem fi WHERE fi.hotel = h AND fi.isAvailable = true)")
    List<Hotel> findActiveHotelsWithAvailableFood();

   

}