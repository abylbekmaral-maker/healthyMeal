package com.example.demo.repository;

import com.example.demo.entity.ShoppingCheck;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ShoppingCheckRepository extends JpaRepository<ShoppingCheck, Long> {

    List<ShoppingCheck> findByWeeklyPlan_Id(Long weeklyPlanId);

    Optional<ShoppingCheck> findByWeeklyPlan_IdAndIngredient_Id(Long weeklyPlanId, Long ingredientId);
}
