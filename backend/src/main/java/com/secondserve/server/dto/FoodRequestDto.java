package com.secondserve.server.dto;

import com.secondserve.server.entity.FoodRequest.RequestStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class FoodRequestDto {
    private Long id;
    private Long ngoId;
    private String ngoName;
    private Long foodItemId;
    private String foodItemName;
    private String hotelName;

    @NotNull(message = "Requested quantity is required")
    @Positive(message = "Requested quantity must be positive")
    private BigDecimal requestedQuantity;

    private RequestStatus requestStatus;
    private LocalDateTime requestDate;
    private LocalDateTime pickupDate;
    private String notes;
    private String unit;

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
    // Constructors
    public FoodRequestDto() {}

    public FoodRequestDto(Long ngoId, Long foodItemId, BigDecimal requestedQuantity) {
        this.ngoId = ngoId;
        this.foodItemId = foodItemId;
        this.requestedQuantity = requestedQuantity;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getNgoId() { return ngoId; }
    public void setNgoId(Long ngoId) { this.ngoId = ngoId; }

    public String getNgoName() { return ngoName; }
    public void setNgoName(String ngoName) { this.ngoName = ngoName; }

    public Long getFoodItemId() { return foodItemId; }
    public void setFoodItemId(Long foodItemId) { this.foodItemId = foodItemId; }

    public String getFoodItemName() { return foodItemName; }
    public void setFoodItemName(String foodItemName) { this.foodItemName = foodItemName; }

    public String getHotelName() { return hotelName; }
    public void setHotelName(String hotelName) { this.hotelName = hotelName; }

    public BigDecimal getRequestedQuantity() { return requestedQuantity; }
    public void setRequestedQuantity(BigDecimal requestedQuantity) { this.requestedQuantity = requestedQuantity; }

    public RequestStatus getRequestStatus() { return requestStatus; }
    public void setRequestStatus(RequestStatus requestStatus) { this.requestStatus = requestStatus; }

    public LocalDateTime getRequestDate() { return requestDate; }
    public void setRequestDate(LocalDateTime requestDate) { this.requestDate = requestDate; }

    public LocalDateTime getPickupDate() { return pickupDate; }
    public void setPickupDate(LocalDateTime pickupDate) { this.pickupDate = pickupDate; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
