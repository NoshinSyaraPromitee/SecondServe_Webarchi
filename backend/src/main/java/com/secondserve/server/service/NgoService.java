package com.secondserve.server.service;

import com.secondserve.server.dto.NgoDto;
import com.secondserve.server.entity.Ngo;
import com.secondserve.server.exception.ResourceNotFoundException;
import com.secondserve.server.repository.NgoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Import for @Transactional
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NgoService {

    @Autowired
    private NgoRepository ngoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * This is the new, dedicated method for handling NGO registration business logic.
     * It was moved here from the old AuthService.
     */
    @Transactional // Ensures the entire method is a single, complete database transaction.
    public NgoDto registerNgo(NgoDto ngoDto) {
        if (ngoRepository.existsByEmail(ngoDto.getEmail())) {
            throw new RuntimeException("Error: Email address is already in use.");
        }

        Ngo ngo = convertToEntity(ngoDto);
        ngo.setPassword(passwordEncoder.encode(ngoDto.getPassword()));

        Ngo savedNgo = ngoRepository.save(ngo);
        return convertToDto(savedNgo);
    }

    public List<NgoDto> getAllActiveNgos() {
        return ngoRepository.findByIsActiveTrue()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public NgoDto getNgoById(Long id) {
        Ngo ngo = ngoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("NGO not found with id: " + id));
        return convertToDto(ngo);
    }

    public NgoDto updateNgo(Long id, NgoDto ngoDto) {
        Ngo ngo = ngoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("NGO not found with id: " + id));

        ngo.setNgoName(ngoDto.getNgoName());
        ngo.setContactPerson(ngoDto.getContactPerson());
        ngo.setPhone(ngoDto.getPhone());
        ngo.setAddress(ngoDto.getAddress());
        ngo.setCity(ngoDto.getCity());
        ngo.setState(ngoDto.getState());
        ngo.setPostalCode(ngoDto.getPostalCode());
        ngo.setLicenseNumber(ngoDto.getLicenseNumber());

        Ngo updatedNgo = ngoRepository.save(ngo);
        return convertToDto(updatedNgo);
    }

    public void deleteNgo(Long id) {
        Ngo ngo = ngoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("NGO not found with id: " + id));
        ngo.setIsActive(false); 
        ngoRepository.save(ngo);
    }

    public Ngo findByEmail(String email) {
        return ngoRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("NGO not found with email: " + email));
    }

    private NgoDto convertToDto(Ngo ngo) {
        NgoDto dto = new NgoDto();
        dto.setId(ngo.getId());
        dto.setNgoName(ngo.getNgoName());
        dto.setContactPerson(ngo.getContactPerson());
        dto.setEmail(ngo.getEmail());
        dto.setPhone(ngo.getPhone());
        dto.setAddress(ngo.getAddress());
        dto.setCity(ngo.getCity());
        dto.setState(ngo.getState());
        dto.setPostalCode(ngo.getPostalCode());
        dto.setLicenseNumber(ngo.getLicenseNumber());
        dto.setRegistrationDate(ngo.getRegistrationDate());
        dto.setIsActive(ngo.getIsActive());
        dto.setTotalFoodReceived(ngo.getTotalFoodReceived());
        
        return dto;
    }

    private Ngo convertToEntity(NgoDto dto) {
        Ngo ngo = new Ngo();
        ngo.setNgoName(dto.getNgoName());
        ngo.setContactPerson(dto.getContactPerson());
        ngo.setEmail(dto.getEmail());
        ngo.setPassword(dto.getPassword());
        ngo.setPhone(dto.getPhone());
        ngo.setAddress(dto.getAddress());
        ngo.setCity(dto.getCity());
        ngo.setState(dto.getState());
        ngo.setPostalCode(dto.getPostalCode());
        ngo.setLicenseNumber(dto.getLicenseNumber());
        return ngo;
    }
}