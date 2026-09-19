package com.secondserve.server.service;

import com.secondserve.server.dto.FoodRequestDto;
import com.secondserve.server.entity.FoodItem;
import com.secondserve.server.entity.FoodRequest;
import com.secondserve.server.entity.FoodRequest.RequestStatus;
import com.secondserve.server.entity.Hotel;
import com.secondserve.server.entity.Ngo;
import com.secondserve.server.exception.ResourceNotFoundException;
import com.secondserve.server.repository.FoodItemRepository;
import com.secondserve.server.repository.FoodRequestRepository;
import com.secondserve.server.repository.HotelRepository;
import com.secondserve.server.repository.NgoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FoodRequestService {

    @Autowired private FoodRequestRepository foodRequestRepository;
    @Autowired private NgoRepository ngoRepository;
    @Autowired private FoodItemRepository foodItemRepository;
    @Autowired private HotelRepository hotelRepository;
    @Transactional
    public FoodRequestDto createFoodRequest(FoodRequestDto foodRequestDto, Long ngoId) {
        Ngo ngo = ngoRepository.findById(ngoId)
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated NGO not found with id: " + ngoId));

        FoodItem foodItem = foodItemRepository.findById(foodRequestDto.getFoodItemId())
                .orElseThrow(() -> new ResourceNotFoundException("Food item not found with id: " + foodRequestDto.getFoodItemId()));

        if (!foodItem.getIsAvailable()) {
            throw new IllegalStateException("This food item is no longer available for donation.");
        }

        FoodRequest foodRequest = convertToEntity(foodRequestDto);
        foodRequest.setNgo(ngo);
        foodRequest.setFoodItem(foodItem);
        foodRequest.setRequestStatus(RequestStatus.PENDING); 

        FoodRequest savedRequest = foodRequestRepository.save(foodRequest);
        return convertToDto(savedRequest);
    }

    @Transactional
    public FoodRequestDto updateRequestStatus(Long id, RequestStatus status) {
        FoodRequest foodRequest = foodRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Food request not found with id: " + id));

        if (foodRequest.getRequestStatus() != RequestStatus.PENDING) {
            throw new IllegalStateException("This request has already been " + foodRequest.getRequestStatus().toString().toLowerCase());
        }

        foodRequest.setRequestStatus(status);

        if (status == RequestStatus.APPROVED) {
            FoodItem foodItem = foodRequest.getFoodItem();
            foodItem.setIsAvailable(false);
            foodItemRepository.save(foodItem);
        }

        FoodRequest updatedRequest = foodRequestRepository.save(foodRequest);
        return convertToDto(updatedRequest);
    }

    public List<FoodRequestDto> getRequestsByNgo(Long ngoId) {
        return foodRequestRepository.findByNgoIdOrderByRequestDateDesc(ngoId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<FoodRequestDto> getRequestsForHotel(Long hotelId) {
        return foodRequestRepository.findByFoodItemHotelIdOrderByRequestDateDesc(hotelId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }


    private FoodRequestDto convertToDto(FoodRequest request) {
        FoodRequestDto dto = new FoodRequestDto();
        dto.setId(request.getId());
        dto.setNgoId(request.getNgo().getId());
        dto.setNgoName(request.getNgo().getNgoName());
        dto.setFoodItemId(request.getFoodItem().getId());
        dto.setFoodItemName(request.getFoodItem().getFoodName());
        dto.setHotelName(request.getFoodItem().getHotel().getHotelName());
        dto.setRequestedQuantity(request.getRequestedQuantity());
        dto.setUnit(request.getFoodItem().getUnit()); // ADD THIS LINE
        dto.setRequestStatus(request.getRequestStatus());
        dto.setRequestDate(request.getRequestDate());
        dto.setNotes(request.getNotes());
        dto.setImageUrl(request.getFoodItem().getImageUrl());
        return dto;
    }

    private FoodRequest convertToEntity(FoodRequestDto dto) {
        FoodRequest request = new FoodRequest();
        request.setRequestedQuantity(dto.getRequestedQuantity());
        request.setNotes(dto.getNotes());
        return request;
    }
    public List<FoodRequestDto> getRequestsByHotelAndStatus(Long hotelId, RequestStatus status) {
       
        String statusString = status.name(); 

        return foodRequestRepository.findRequestsByHotelAndStatusNative(hotelId, statusString)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }


    @Transactional
    public void completeFoodRequest(Long requestId) {
        
        FoodRequest foodRequest = foodRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Food request not found with id: " + requestId));

        if (foodRequest.getRequestStatus() != RequestStatus.APPROVED) {
            throw new IllegalStateException("Cannot complete a request that is not in APPROVED status.");
        }

        foodRequest.setRequestStatus(RequestStatus.COMPLETED);

        Hotel hotel = foodRequest.getFoodItem().getHotel();
        if (hotel != null) {
            hotel.addToTotalDonated(foodRequest.getRequestedQuantity());

            hotelRepository.save(hotel);
        }

        foodRequestRepository.save(foodRequest);
    }
}