package com.example.demo;

import java.util.List;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface DailyMealItemRepository extends JpaRepository<DailyMealItem, Long> {

    @Modifying
    @Transactional
    @Query("delete from DailyMealItem i where i.dailyMeal.weeklyPlan.id = :planId")
    void deleteByWeeklyPlanId(@Param("planId") Long planId);

	List<DailyMealItem> findByDailyMealOrderById(DailyMeal dailyMeal);
}
