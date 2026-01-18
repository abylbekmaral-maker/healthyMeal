package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface HealthTrackerRepository extends JpaRepository<HealthTrackerEntry, Long> {

    Optional<HealthTrackerEntry> findByUserIdAndEntryDate(Long userId, LocalDate targetDate);

    List<HealthTrackerEntry> findByUserIdAndEntryDateBetween(Long userId,
                                                             LocalDate start,
                                                             LocalDate end);
}
