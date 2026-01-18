package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface WeeklyPlanRepository extends JpaRepository<WeeklyPlan, Long> {

    Optional<WeeklyPlan>
    findTopByUserAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByStartDateDesc(
            User user, LocalDate start, LocalDate end
    );
}
