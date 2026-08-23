package com.secondserve.server.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "food_items_table")
public class FoodItem {

    public enum Category {
        PREPARED_FOOD,
        INGREDIENTS
    }

    public enum Condition {
        FRESH,
        GOOD,
        NEAR_EXPIRY
    }

    // --- FIELDS ---
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id", nullable = false)
    private Hotel hotel;

    @NotBlank
    @Column(name = "food_name", nullable = false)
    private String foodName;

    @NotNull
    @Column(nullable = false)
    private BigDecimal quantity;

    @NotBlank
    @Column(nullable = false)
    private String unit;

    @NotNull
    @Column(name = "expiry_date", nullable = false)
    private LocalDate expiryDate;

    private String description;

    @Column(name = "is_available", nullable = false)
    private Boolean isAvailable = false;

    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate = LocalDateTime.now();

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "`condition`", nullable = false)
    private Condition condition;

    @OneToMany(mappedBy = "foodItem", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<FoodRequest> foodRequests;


    // --- CONSTRUCTORS & METHODS ---
    public FoodItem() {}

    // Getters and Setters for all fields...
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Hotel getHotel() { return hotel; }
    public void setHotel(Hotel hotel) { this.hotel = hotel; }

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
    public void setIsAvailable(Boolean available) { isAvailable = available; }

    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    public Condition getCondition() { return condition; }
    public void setCondition(Condition condition) { this.condition = condition; }

    public List<FoodRequest> getFoodRequests() { return foodRequests; }
    public void setFoodRequests(List<FoodRequest> foodRequests) { this.foodRequests = foodRequests; }
}