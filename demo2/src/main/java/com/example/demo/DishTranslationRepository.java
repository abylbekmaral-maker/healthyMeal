package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface DishTranslationRepository extends JpaRepository<DishTranslation, Long> {
    Optional<DishTranslation> findByDish_IdAndLanguage(Long dishId, LanguageCode language);
    Optional<DishTranslation> findByNameAndLanguage(String name, LanguageCode language);

    // ✅ 추가: 언어 상관없이 이름으로 먼저 매칭
    Optional<DishTranslation> findFirstByNameIgnoreCase(String name);
}
