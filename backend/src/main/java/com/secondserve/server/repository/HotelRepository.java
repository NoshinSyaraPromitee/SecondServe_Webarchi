package com.secondserve.server.repository;

import com.secondserve.server.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {

    Optional<Hotel> findByEmail(String email);

    boolean existsByEmail(String email);


    Optional<Hotel> findByHotelCode(String hotelCode);

    boolean existsByHotelCode(String hotelCode);


    List<Hotel> findByIsActiveTrue();

    @Query("SELECT h FROM Hotel h WHERE h.isActive = true AND EXISTS " +
            "(SELECT 1 FROM FoodItem fi WHERE fi.hotel = h AND fi.isAvailable = true)")
    List<Hotel> findActiveHotelsWithAvailableFood();

   

}