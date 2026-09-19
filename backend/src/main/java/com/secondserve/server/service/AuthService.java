package com.secondserve.server.service;

import com.secondserve.server.dto.AuthResponse;
import com.secondserve.server.dto.LoginRequest;
import com.secondserve.server.entity.Hotel;
import com.secondserve.server.entity.KitchenStaff; 
import com.secondserve.server.entity.Ngo;
import com.secondserve.server.exception.ResourceNotFoundException;
import com.secondserve.server.repository.HotelRepository;
import com.secondserve.server.repository.KitchenStaffRepository; 
import com.secondserve.server.repository.NgoRepository;
import com.secondserve.server.config.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private NgoRepository ngoRepository;

   
    @Autowired
    private KitchenStaffRepository kitchenStaffRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

   

    public AuthResponse login(LoginRequest loginRequest) {
        if (loginRequest.getUserType() == null) {
            throw new RuntimeException("User type must be provided.");
        }

        String userType = loginRequest.getUserType().toUpperCase();

        switch (userType) {
            case "HOTEL_MANAGER":
                Hotel hotel = hotelRepository.findByEmail(loginRequest.getEmail())
                        .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + loginRequest.getEmail()));

                if (!passwordEncoder.matches(loginRequest.getPassword(), hotel.getPassword())) {
                    throw new RuntimeException("Invalid credentials");
                }
                if (!hotel.getIsActive()) {
                    throw new RuntimeException("Account is inactive");
                }

                String hotelToken = jwtUtil.generateToken(hotel.getEmail(), "HOTEL_MANAGER", hotel.getId());

    
                
                AuthResponse hotelResponse = new AuthResponse(hotelToken, "HOTEL_MANAGER", hotel.getId(), hotel.getManagerName(), hotel.getEmail());
                hotelResponse.setOrganizationName(hotel.getHotelName()); 
                return hotelResponse;

            case "NGO":
                Ngo ngo = ngoRepository.findByEmail(loginRequest.getEmail())
                        .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + loginRequest.getEmail()));

                if (!passwordEncoder.matches(loginRequest.getPassword(), ngo.getPassword())) {
                    throw new RuntimeException("Invalid credentials");
                }
                if (!ngo.getIsActive()) {
                    throw new RuntimeException("Account is inactive");
                }

                String ngoToken = jwtUtil.generateToken(ngo.getEmail(), "NGO", ngo.getId());

                AuthResponse ngoResponse = new AuthResponse(ngoToken, "NGO", ngo.getId(), ngo.getContactPerson(), ngo.getEmail());
                ngoResponse.setOrganizationName(ngo.getNgoName()); 
                return ngoResponse;

            case "KITCHEN_STAFF":
                KitchenStaff staff = kitchenStaffRepository.findByEmail(loginRequest.getEmail())
                        .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + loginRequest.getEmail()));

                if (!passwordEncoder.matches(loginRequest.getPassword(), staff.getPassword())) {
                    throw new RuntimeException("Invalid credentials");
                }
                if (!staff.getIsActive()) {
                    throw new RuntimeException("Account is inactive");
                }

                String staffToken = jwtUtil.generateToken(staff.getEmail(), "KITCHEN_STAFF", staff.getId());

               
                AuthResponse staffResponse = new AuthResponse(staffToken, "KITCHEN_STAFF", staff.getId(), staff.getStaffName(), staff.getEmail());
                staffResponse.setOrganizationName(staff.getHotel().getHotelName());
                return staffResponse;

            default:
                throw new RuntimeException("Invalid user type");
        }
    }
}