package com.example.demo.repository;


import com.example.demo.model.Rug;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RugRepo extends JpaRepository<Rug, UUID> {
    Optional<Rug> findById(String id);
}