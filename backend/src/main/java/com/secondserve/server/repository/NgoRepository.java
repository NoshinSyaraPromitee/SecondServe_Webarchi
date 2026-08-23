package com.secondserve.server.repository;

import com.secondserve.server.entity.Ngo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface NgoRepository extends JpaRepository<Ngo, Long> {

    // --- METHODS FOR AUTHENTICATION & REGISTRATION ---

    /**
     * Finds an NGO by their unique email address.
     * This is used by the AuthService for the login process.
     */
    Optional<Ngo> findByEmail(String email);

    /**
     * Quickly checks if an NGO with the given email already exists.
     * This is used by the NgoService during registration to prevent duplicate accounts.
     */
    boolean existsByEmail(String email);


    // --- METHOD FOR GENERAL BROWSING ---

    /**
     * Retrieves a list of all NGOs that are currently active.
     * This can be used for administrative purposes or for hotels to view potential partners.
     */
    List<Ngo> findByIsActiveTrue();


    

}