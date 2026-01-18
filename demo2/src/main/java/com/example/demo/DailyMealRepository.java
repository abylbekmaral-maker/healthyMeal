package com.example.demo;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface DailyMealRepository extends JpaRepository<DailyMeal, Long> {

    Optional<DailyMeal> findByWeeklyPlanAndDate(WeeklyPlan weeklyPlan, LocalDate date);
    @Modifying
    @Transactional
    @Query("delete from DailyMeal d where d.weeklyPlan.id = :planId")
    void deleteByWeeklyPlanId(@Param("planId") Long planId);
	List<DailyMeal> findByWeeklyPlanOrderByDate(WeeklyPlan plan);

}
