package com.example.demo.repository;

import com.example.demo.entity.Ingredient;
import com.example.demo.entity.IngredientTranslation;
import com.example.demo.entity.LanguageCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IngredientTranslationRepository extends JpaRepository<IngredientTranslation, Long> {

    Optional<IngredientTranslation> findByIngredientAndLanguage(Ingredient ingredient, LanguageCode language);

    Optional<IngredientTranslation> findByIngredient_IdAndLanguage(Long ingredientId, LanguageCode language);
}
