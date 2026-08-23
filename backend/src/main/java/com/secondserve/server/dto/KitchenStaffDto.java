package com.secondserve.server.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class KitchenStaffDto {

    @NotBlank(message = "Staff name is required")
    private String staffName;

    @Email(message = "Please provide a valid email")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;

    @NotBlank(message = "Hotel code is required")
    private String hotelCode;

    // Position is optional
    private String position;

    // Getters and Setters
    public String getStaffName() { return staffName; }
    public void setStaffName(String staffName) { this.staffName = staffName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getHotelCode() { return hotelCode; }
    public void setHotelCode(String hotelCode) { this.hotelCode = hotelCode; }
    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }
}