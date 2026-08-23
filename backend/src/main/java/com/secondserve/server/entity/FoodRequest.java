package com.secondserve.server.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "food_requests_table")
public class FoodRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ngo_id", nullable = false)
    private Ngo ngo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_item_id", nullable = false)
    private FoodItem foodItem;

    @NotNull
    @Column(name = "requested_quantity", nullable = false)
    private BigDecimal requestedQuantity;

    @Column(name = "request_status")
    @Enumerated(EnumType.STRING)
    private RequestStatus requestStatus = RequestStatus.PENDING;

    @Column(name = "request_date")
    private LocalDateTime requestDate = LocalDateTime.now();

    @Column(name = "pickup_date")
    private LocalDateTime pickupDate;

    private String notes;

    public enum RequestStatus {
        PENDING, APPROVED, REJECTED, COMPLETED
    }

    // Constructors
    public FoodRequest() {}

    public FoodRequest(Ngo ngo, FoodItem foodItem, BigDecimal requestedQuantity) {
        this.ngo = ngo;
        this.foodItem = foodItem;
        this.requestedQuantity = requestedQuantity;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Ngo getNgo() { return ngo; }
    public void setNgo(Ngo ngo) { this.ngo = ngo; }

    public FoodItem getFoodItem() { return foodItem; }
    public void setFoodItem(FoodItem foodItem) { this.foodItem = foodItem; }

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