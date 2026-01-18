package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {}
