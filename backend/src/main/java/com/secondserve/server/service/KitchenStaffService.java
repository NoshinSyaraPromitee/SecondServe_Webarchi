package com.secondserve.server.service;
import com.secondserve.server.config.JwtUtil;

import com.secondserve.server.dto.AuthResponse;
import com.secondserve.server.dto.KitchenStaffDto;
import com.secondserve.server.entity.Hotel;
import com.secondserve.server.entity.KitchenStaff;
import com.secondserve.server.exception.ResourceNotFoundException;
import com.secondserve.server.repository.HotelRepository;
import com.secondserve.server.repository.KitchenStaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class KitchenStaffService {

    @Autowired private KitchenStaffRepository kitchenStaffRepository;
    @Autowired private HotelRepository hotelRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private JwtUtil jwtUtil;

    @Transactional
    public AuthResponse registerStaff(KitchenStaffDto staffDto) {
        if (kitchenStaffRepository.existsByEmail(staffDto.getEmail())) {
            throw new RuntimeException("Email address is already in use.");
        }

        // Find the hotel using the code provided by the staff member
        Hotel hotel = hotelRepository.findByHotelCode(staffDto.getHotelCode())
                .orElseThrow(() -> new ResourceNotFoundException("Invalid Hotel Code: " + staffDto.getHotelCode()));

        KitchenStaff newStaff = new KitchenStaff();
        newStaff.setStaffName(staffDto.getStaffName());
        newStaff.setEmail(staffDto.getEmail());
        newStaff.setPassword(passwordEncoder.encode(staffDto.getPassword()));
        newStaff.setPosition(staffDto.getPosition());
        newStaff.setHotel(hotel); // Establish the link to the hotel

        KitchenStaff savedStaff = kitchenStaffRepository.save(newStaff);

        // Generate a token for immediate login after registration
        String token = jwtUtil.generateToken(savedStaff.getEmail(), "KITCHEN_STAFF", savedStaff.getId());

        return new AuthResponse(token, "KITCHEN_STAFF", savedStaff.getId(), savedStaff.getStaffName(), savedStaff.getEmail());
    }
}