package com.secondserve.server.service;

import com.secondserve.server.dto.DashboardStatsDto;
import com.secondserve.server.dto.HotelDto;
import com.secondserve.server.entity.Hotel;
import com.secondserve.server.exception.ResourceNotFoundException;
import com.secondserve.server.repository.FoodItemRepository;
import com.secondserve.server.repository.FoodRequestRepository;
import com.secondserve.server.repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class HotelService {

    @Autowired private HotelRepository hotelRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private FoodItemRepository foodItemRepository;
    @Autowired private FoodRequestRepository foodRequestRepository;

    public DashboardStatsDto getDashboardStats(Long hotelId) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + hotelId));

       

        // Calculate 'logged' food as before
        LocalDateTime startOfWeek = LocalDateTime.now().with(DayOfWeek.MONDAY).toLocalDate().atStartOfDay();
        Double loggedSum = foodItemRepository.sumQuantityByHotelAndDateRange(hotelId, startOfWeek);

        DashboardStatsDto statsDto = new DashboardStatsDto();

        // Get the 'donated' total directly from the hotel's stored field.
        
        statsDto.setTotalDonatedThisWeek(hotel.getTotalFoodDonated());

        // 'Logged this week' remains the same
        statsDto.setTotalLoggedThisWeek(BigDecimal.valueOf(loggedSum != null ? loggedSum : 0.0));

        // Hotel code is correct
        statsDto.setHotelCode(hotel.getHotelCode());

        return statsDto;
    }

    @Transactional
    public HotelDto registerHotel(HotelDto hotelDto) {
        if (hotelRepository.existsByEmail(hotelDto.getEmail())) {
            throw new RuntimeException("Error: Email address is already in use.");
        }
        Hotel newHotel = convertToEntity(hotelDto);
        newHotel.setPassword(passwordEncoder.encode(hotelDto.getPassword()));
        newHotel.setHotelCode(generateUniqueHotelCode());
        Hotel savedHotel = hotelRepository.save(newHotel);
        return convertToDto(savedHotel);
    }

    public List<HotelDto> getAllActiveHotelsWithDonations() {
        return hotelRepository.findActiveHotelsWithAvailableFood()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public HotelDto getHotelById(Long id) {
        Hotel hotel = hotelRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + id));
        return convertToDto(hotel);
    }

    public HotelDto updateHotel(Long id, HotelDto hotelDto) {
        Hotel hotel = hotelRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + id));
        hotel.setManagerName(hotelDto.getManagerName());
        hotel.setPhone(hotelDto.getPhone());
        hotel.setAddress(hotelDto.getAddress());
        Hotel updatedHotel = hotelRepository.save(hotel);
        return convertToDto(updatedHotel);
    }

    public void deleteHotel(Long id) {
        Hotel hotel = hotelRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + id));
        hotel.setIsActive(false);
        hotelRepository.save(hotel);
    }

    private String generateUniqueHotelCode() {
        String code;
        Random random = new Random();
        do {
            int number = 100 + random.nextInt(900);
            code = "HTL-" + number;
        } while (hotelRepository.existsByHotelCode(code));
        return code;
    }

    private HotelDto convertToDto(Hotel hotel) {
        HotelDto dto = new HotelDto();
        dto.setId(hotel.getId());
        dto.setHotelName(hotel.getHotelName());
        dto.setManagerName(hotel.getManagerName());
        dto.setEmail(hotel.getEmail());
        dto.setPhone(hotel.getPhone());
        dto.setAddress(hotel.getAddress());
        dto.setCity(hotel.getCity());
        dto.setState(hotel.getState());
        dto.setPostalCode(hotel.getPostalCode());
        dto.setHotelLicense(hotel.getHotelLicense());
        dto.setHotelCode(hotel.getHotelCode());
        dto.setRegistrationDate(hotel.getRegistrationDate());
        return dto;
    }

    private Hotel convertToEntity(HotelDto dto) {
        Hotel hotel = new Hotel();
        hotel.setHotelName(dto.getHotelName());
        hotel.setManagerName(dto.getManagerName());
        hotel.setEmail(dto.getEmail());
        hotel.setPhone(dto.getPhone());
        hotel.setAddress(dto.getAddress());
        hotel.setHotelLicense(dto.getHotelLicense());
        return hotel;
    }
}