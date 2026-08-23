package com.secondserve.server.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal; // Import for totalFoodDonated if you want to include it in the response
import java.time.LocalDateTime;

public class HotelDto {

    private Long id;

    @NotBlank(message = "Hotel name is required")
    private String hotelName;
    @NotBlank(message = "Manager name is required")
    private String managerName;
    
    private String email;
    
    private String password;
    
    private String address;
    
    private String hotelLicense;

    
    private String hotelCode;

    private String phone;
    private String city;
    private String state;
    private String postalCode;
    private LocalDateTime registrationDate;

    // --- CONSTRUCTORS, GETTERS, AND SETTERS ---
    public HotelDto() {}

    // You can generate all the getters and setters automatically in your IDE

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

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getHotelLicense() { return hotelLicense; }
    public void setHotelLicense(String hotelLicense) { this.hotelLicense = hotelLicense; }

    public String getHotelCode() { return hotelCode; }
    public void setHotelCode(String hotelCode) { this.hotelCode = hotelCode; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getPostalCode() { return postalCode; }
    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }

    public LocalDateTime getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(LocalDateTime registrationDate) { this.registrationDate = registrationDate; }
}