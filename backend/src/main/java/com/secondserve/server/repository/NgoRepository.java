package com.secondserve.server.repository;

import com.secondserve.server.entity.Ngo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface NgoRepository extends JpaRepository<Ngo, Long> {

    Optional<Ngo> findByEmail(String email);


    boolean existsByEmail(String email);

    List<Ngo> findByIsActiveTrue();


    

}