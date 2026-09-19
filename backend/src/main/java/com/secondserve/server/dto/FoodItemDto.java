package com.secondserve.server.dto;

import com.secondserve.server.entity.FoodItem; 
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;


public class FoodItemDto {


    private Long id;
    private Long hotelId; 
    private String hotelName;
    private Boolean isAvailable;
    private LocalDateTime createdDate;
    private String currentUserRequestStatus;

    @NotBlank(message = "Food name is required")
    private String foodName;

    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be positive")
    private BigDecimal quantity;

    @NotBlank(message = "Unit is required") 
    private String unit;

    @NotNull(message = "Expiry date is required")
    private LocalDate expiryDate;

    private String reviewStatus;
    private String description;

    private String imageUrl;

    @NotNull(message = "Category is required")
    private FoodItem.Category category;

    @NotNull(message = "Condition is required")
    private FoodItem.Condition condition;

   


    // --- Constructors, Getters, and Setters ---
    public FoodItemDto() {}
    

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getHotelId() { return hotelId; }
    
    public void setHotelId(Long hotelId) { this.hotelId = hotelId; }

    public String getHotelName() { return hotelName; }
    public void setHotelName(String hotelName) { this.hotelName = hotelName; }

    public String getFoodName() { return foodName; }
    public void setFoodName(String foodName) { this.foodName = foodName; }

    public BigDecimal getQuantity() { return quantity; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Boolean getIsAvailable() { return isAvailable; }
    public void setIsAvailable(Boolean isAvailable) { this.isAvailable = isAvailable; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }

    public FoodItem.Category getCategory() { return category; }
    public void setCategory(FoodItem.Category category) { this.category = category; }

    public FoodItem.Condition getCondition() { return condition; }
    public void setCondition(FoodItem.Condition condition) { this.condition = condition; }

    public String getReviewStatus() { return reviewStatus; }
    public void setReviewStatus(String reviewStatus) { this.reviewStatus = reviewStatus; }
    
    public String getCurrentUserRequestStatus() {
        return currentUserRequestStatus;
    }

    public void setCurrentUserRequestStatus(String currentUserRequestStatus) {
        this.currentUserRequestStatus = currentUserRequestStatus;
    }
}