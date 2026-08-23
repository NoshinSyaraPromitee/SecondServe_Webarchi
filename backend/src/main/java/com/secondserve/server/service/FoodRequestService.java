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
    /**
     * Creates a new food request from an NGO for a specific food item.
     * @param foodRequestDto DTO containing details like foodItemId and requestedQuantity.
     * @param ngoId The ID of the authenticated NGO making the request (for security).
     * @return A DTO of the newly created request, which will have a 'PENDING' status.
     */
    @Transactional
    public FoodRequestDto createFoodRequest(FoodRequestDto foodRequestDto, Long ngoId) {
        // Find the NGO using the secure ID from the authenticated user's token
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
        foodRequest.setRequestStatus(RequestStatus.PENDING); // Explicitly set the initial status

        FoodRequest savedRequest = foodRequestRepository.save(foodRequest);
        return convertToDto(savedRequest);
    }

    /**
     * Updates the status of a request (e.g., from PENDING to APPROVED or REJECTED).
     * This action is typically performed by a Hotel Manager.
     * @param id The ID of the food request to update.
     * @param status The new status (APPROVED or REJECTED).
     * @return A DTO of the updated food request.
     */
    @Transactional
    public FoodRequestDto updateRequestStatus(Long id, RequestStatus status) {
        FoodRequest foodRequest = foodRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Food request not found with id: " + id));

        // This is a crucial business rule: only pending requests can be approved or rejected.
        if (foodRequest.getRequestStatus() != RequestStatus.PENDING) {
            throw new IllegalStateException("This request has already been " + foodRequest.getRequestStatus().toString().toLowerCase());
        }

        foodRequest.setRequestStatus(status);

        // Another key rule: if approved, the food item is now "off the market".
        if (status == RequestStatus.APPROVED) {
            FoodItem foodItem = foodRequest.getFoodItem();
            foodItem.setIsAvailable(false);
            foodItemRepository.save(foodItem);
        }

        FoodRequest updatedRequest = foodRequestRepository.save(foodRequest);
        return convertToDto(updatedRequest);
    }

    /**
     * Retrieves the full request history for a specific NGO.
     * This is used to power the NGO's "My Requests" page.
     */
    public List<FoodRequestDto> getRequestsByNgo(Long ngoId) {
        return foodRequestRepository.findByNgoIdOrderByRequestDateDesc(ngoId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves the full request history for a specific hotel.
     * This can be used to show a hotel manager all past and present requests.
     */
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
        return dto;
    }

    private FoodRequest convertToEntity(FoodRequestDto dto) {
        FoodRequest request = new FoodRequest();
        // NOTE: We only map fields that the client is allowed to provide.
        // ngoId and foodItemId are looked up separately for security.
        request.setRequestedQuantity(dto.getRequestedQuantity());
        request.setNotes(dto.getNotes());
        return request;
    }
    public List<FoodRequestDto> getRequestsByHotelAndStatus(Long hotelId, RequestStatus status) {
        // Convert the enum to a string for the native query
        String statusString = status.name(); // This will be "PENDING"

        // Call the new repository method that ends with "Native"
        return foodRequestRepository.findRequestsByHotelAndStatusNative(hotelId, statusString)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }


    @Transactional
    public void completeFoodRequest(Long requestId) {
        // Step 1: Find the request
        FoodRequest foodRequest = foodRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Food request not found with id: " + requestId));

        // Step 2: Check if the request is in the correct state to be completed
        if (foodRequest.getRequestStatus() != RequestStatus.APPROVED) {
            throw new IllegalStateException("Cannot complete a request that is not in APPROVED status.");
        }

        // Step 3: Update the request's status
        foodRequest.setRequestStatus(RequestStatus.COMPLETED);

        // Step 4: Get the associated hotel and update its donation total
        Hotel hotel = foodRequest.getFoodItem().getHotel();
        if (hotel != null) {
            // Use the helper method we created in the Hotel entity
            hotel.addToTotalDonated(foodRequest.getRequestedQuantity());

            // Save the hotel with the updated total
            hotelRepository.save(hotel);
        }

        // Step 5: Save the updated request
        foodRequestRepository.save(foodRequest);
    }
}