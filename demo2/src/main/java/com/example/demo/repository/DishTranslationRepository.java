package com.example.demo.repository;

import com.example.demo.entity.DishTranslation;
import com.example.demo.entity.LanguageCode;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface DishTranslationRepository extends JpaRepository<DishTranslation, Long> {
    Optional<DishTranslation> findByDish_IdAndLanguage(Long dishId, LanguageCode language);
    Optional<DishTranslation> findByNameAndLanguage(String name, LanguageCode language);

    Optional<DishTranslation> findFirstByNameIgnoreCase(String name);
}
