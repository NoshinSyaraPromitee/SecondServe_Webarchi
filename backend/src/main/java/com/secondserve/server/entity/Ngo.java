package com.secondserve.server.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "ngo_table")
public class Ngo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "ngo_name", nullable = false)
    private String ngoName;

    @NotBlank
    @Column(name = "contact_person", nullable = false)
    private String contactPerson;

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

    @Column(name = "license_number")
    private String licenseNumber;

    @Column(name = "registration_date")
    private LocalDateTime registrationDate = LocalDateTime.now();

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "total_food_received")
    private BigDecimal totalFoodReceived = BigDecimal.ZERO;

    @OneToMany(mappedBy = "ngo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<FoodRequest> foodRequests;

    // Constructors
    public Ngo() {}

    public Ngo(String ngoName, String contactPerson, String email, String password) {
        this.ngoName = ngoName;
        this.contactPerson = contactPerson;
        this.email = email;
        this.password = password;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNgoName() { return ngoName; }
    public void setNgoName(String ngoName) { this.ngoName = ngoName; }

    public String getContactPerson() { return contactPerson; }
    public void setContactPerson(String contactPerson) { this.contactPerson = contactPerson; }

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

    public String getLicenseNumber() { return licenseNumber; }
    public void setLicenseNumber(String licenseNumber) { this.licenseNumber = licenseNumber; }

    public LocalDateTime getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(LocalDateTime registrationDate) { this.registrationDate = registrationDate; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public BigDecimal getTotalFoodReceived() { return totalFoodReceived; }
    public void setTotalFoodReceived(BigDecimal totalFoodReceived) { this.totalFoodReceived = totalFoodReceived; }

    public List<FoodRequest> getFoodRequests() { return foodRequests; }
    public void setFoodRequests(List<FoodRequest> foodRequests) { this.foodRequests = foodRequests; }
}
