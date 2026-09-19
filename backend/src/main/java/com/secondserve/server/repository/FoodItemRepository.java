package com.secondserve.server.repository;

import com.secondserve.server.entity.FoodItem;
import com.secondserve.server.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FoodItemRepository extends JpaRepository<FoodItem, Long> {

    List<FoodItem> findByIsAvailableTrueOrderByCreatedDateDesc();
    List<FoodItem> findByHotelAndIsAvailableTrue(Hotel hotel);
    List<FoodItem> findByHotelIdAndIsAvailableTrue(Long hotelId);
    List<FoodItem> findByHotelAndIsAvailableFalseOrderByCreatedDateDesc(Hotel hotel);
    List<FoodItem> findByHotelIdAndIsAvailableFalse(Long hotelId);
    List<FoodItem> findByHotelIdOrderByCreatedDateDesc(Long hotelId);
    List<FoodItem> findByHotelIdAndReviewStatus(Long hotelId, FoodItem.ReviewStatus reviewStatus);

    @Query("SELECT f FROM FoodItem f WHERE f.isAvailable = true AND f.expiryDate > :currentDate ORDER BY f.expiryDate ASC")
    List<FoodItem> findAvailableAndNotExpired(@Param("currentDate") LocalDate currentDate);

    @Query("SELECT COALESCE(SUM(fi.quantity), 0.0) FROM FoodItem fi WHERE fi.hotel.id = :hotelId AND fi.createdDate >= :startDate")
    Double sumQuantityByHotelAndDateRange(@Param("hotelId") Long hotelId, @Param("startDate") LocalDateTime startDate);

    @Query("SELECT f FROM FoodItem f WHERE f.hotel.id = :hotelId AND f.createdDate >= :startOfDay AND f.createdDate < :endOfDay ORDER BY f.createdDate DESC")
    List<FoodItem> findByHotelIdAndCreatedDateToday(
            @Param("hotelId") Long hotelId,
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay
    );
}