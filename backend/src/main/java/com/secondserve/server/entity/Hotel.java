package com.secondserve.server.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList; // --- ADDED ---
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "hotel_manager_table")
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    

    @NotBlank
    @Column(name = "hotel_name", nullable = false)
    private String hotelName;

    @NotBlank
    @Column(name = "manager_name", nullable = false)
    private String managerName;

    @Email
    @NotBlank
    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank
    @Column(nullable = false)
    private String password;

    private String phone;
    private String address;
    private String city;
    private String state;

    @Column(name = "postal_code")
    private String postalCode;

    @Column(name = "registration_date", updatable = false)
    private LocalDateTime registrationDate = LocalDateTime.now();

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "total_food_donated")
    private BigDecimal totalFoodDonated = BigDecimal.ZERO;

    
    @NotBlank
    @Column(name = "hotel_license", nullable = false, unique = true)
    private String hotelLicense;

    
    @Column(name = "hotel_code", unique = true)
    private String hotelCode;

    
    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<KitchenStaff> staff = new ArrayList<>();

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<FoodItem> foodItems;


    // --- Constructors ---
    public Hotel() {}

    public Hotel(String hotelName, String managerName, String email, String password) {
        this.hotelName = hotelName;
        this.managerName = managerName;
        this.email = email;
        this.password = password;
    }




    public String getHotelLicense() {
        return hotelLicense;
    }

    public void setHotelLicense(String hotelLicense) {
        this.hotelLicense = hotelLicense;
    }

    public String getHotelCode() {
        return hotelCode;
    }

    public void setHotelCode(String hotelCode) {
        this.hotelCode = hotelCode;
    }

    public List<KitchenStaff> getStaff() {
        return staff;
    }

    public void setStaff(List<KitchenStaff> staff) {
        this.staff = staff;
    }
    public void addToTotalDonated(BigDecimal amount) {
        if (this.totalFoodDonated == null) {
            this.totalFoodDonated = BigDecimal.ZERO;
        }
        this.totalFoodDonated = this.totalFoodDonated.add(amount);
    }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getHotelName() { return hotelName; }
    public void setHotelName(String hotelName) { this.hotelName = hotelName; }
    public String getManagerName() { return managerName; }
    public void setManagerName(String managerName) { this.managerName = managerName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getState() { return state; }
    public void setState(String state) { this.state = state; }
    public String getPostalCode() { return postalCode; }
    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }
    public LocalDateTime getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(LocalDateTime registrationDate) { this.registrationDate = registrationDate; }
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    public BigDecimal getTotalFoodDonated() { return totalFoodDonated; }
    public void setTotalFoodDonated(BigDecimal totalFoodDonated) { this.totalFoodDonated = totalFoodDonated; }
    public List<FoodItem> getFoodItems() { return foodItems; }
    public void setFoodItems(List<FoodItem> foodItems) { this.foodItems = foodItems; }
}