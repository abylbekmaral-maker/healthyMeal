package com.example.demo.repository;

import com.example.demo.entity.HealthProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface HealthProfileRepository extends JpaRepository<HealthProfile, Integer> {
    Optional<HealthProfile> findByUserId(Integer userId);
}
