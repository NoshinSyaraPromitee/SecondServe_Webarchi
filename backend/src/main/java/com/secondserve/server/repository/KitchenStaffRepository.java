package com.secondserve.server.repository;

import com.secondserve.server.entity.KitchenStaff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface KitchenStaffRepository extends JpaRepository<KitchenStaff, Long> {

    // Used for login
    Optional<KitchenStaff> findByEmail(String email);

    // Used during registration to prevent duplicate accounts
    boolean existsByEmail(String email);
}