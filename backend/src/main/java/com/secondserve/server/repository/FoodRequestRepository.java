package com.secondserve.server.repository;

import com.secondserve.server.entity.FoodRequest;
import com.secondserve.server.entity.FoodRequest.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface FoodRequestRepository extends JpaRepository<FoodRequest, Long> {

    List<FoodRequest> findByNgoIdOrderByRequestDateDesc(Long ngoId);
    List<FoodRequest> findByFoodItemHotelIdOrderByRequestDateDesc(Long hotelId);


    @Query(

            value = "SELECT fr.* FROM food_requests_table fr " +
                    "JOIN food_items_table fi ON fr.food_item_id = fi.id " +
                    "WHERE fi.hotel_id = :hotelId AND fr.request_status = :status " +
                    "ORDER BY fr.request_date ASC",
            nativeQuery = true
    )
    List<FoodRequest> findRequestsByHotelAndStatusNative(@Param("hotelId") Long hotelId, @Param("status") String status);


    @Query(
            value = "SELECT COALESCE(SUM(fr.requested_quantity), 0.0) FROM food_requests_table fr " +
                    "JOIN food_items_table fi ON fr.food_item_id = fi.id " +
                    "WHERE fi.hotel_id = :hotelId AND fr.request_status = 'COMPLETED' AND fr.request_date >= :startDate",
            nativeQuery = true
    )
    Double sumDonatedQuantityByHotelAndDateRange(@Param("hotelId") Long hotelId, @Param("startDate") LocalDateTime startDate);
    Optional<FoodRequest> findTopByFoodItemIdAndNgoIdOrderByRequestDateDesc(Long foodItemId, Long ngoId);
}