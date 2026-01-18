package com.example.demo;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class MealPlanGeneratorService {

    private final DishRepository dishRepository;
    private final DishTranslationRepository dishTranslationRepository;

    // ✅ NEW
    private final DishIngredientRepository dishIngredientRepository;
    private final IngredientTranslationRepository ingredientTranslationRepository;

    public MealPlanGeneratorService(
            DishRepository dishRepository,
            DishTranslationRepository dishTranslationRepository,
            DishIngredientRepository dishIngredientRepository,
            IngredientTranslationRepository ingredientTranslationRepository
    ) {
        this.dishRepository = dishRepository;
        this.dishTranslationRepository = dishTranslationRepository;
        this.dishIngredientRepository = dishIngredientRepository;
        this.ingredientTranslationRepository = ingredientTranslationRepository;
    }

    public PlannerPlanDto generatePreview(Long userId, LanguageCode language) {

        List<Dish> dishes = dishRepository.findByActiveTrue();
        if (dishes == null) dishes = new ArrayList<>();

        if (dishes.isEmpty()) {
            throw new RuntimeException("Dish table empty. Seed data first.");
        }

        LocalDate start = LocalDate.now();

        PlannerPlanDto plan = new PlannerPlanDto();
        plan.setStartDate(start);
        plan.setEndDate(start.plusDays(6));

        List<PlannerPlanDto.PlannerDayDto> days = new ArrayList<>();
        Random r = new Random();

        // ✅ preview에 들어간 dishIds 수집 -> shoppingList 생성
        List<Long> pickedDishIds = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            PlannerPlanDto.PlannerDayDto day = new PlannerPlanDto.PlannerDayDto();
            day.setDate(start.plusDays(i));

            List<AiMealDto> meals = new ArrayList<>();
            List<String> mealTitles = new ArrayList<>();

            for (MealType mt : MealType.values()) {
                Dish dish = dishes.get(r.nextInt(dishes.size()));
                if (dish != null && dish.getId() != null) pickedDishIds.add(dish.getId());

                DishTranslation tr = (dish == null) ? null : dishTranslationRepository
                        .findByDish_IdAndLanguage(dish.getId(), language)
                        .orElse(null);

                String title = (tr != null && tr.getName() != null && !tr.getName().isBlank())
                        ? tr.getName()
                        : "Meal";

                AiMealDto m = new AiMealDto();
                m.setMealType(mt.name());
                m.setTime(mt == MealType.BREAKFAST ? "08:00"
                        : mt == MealType.LUNCH ? "13:00" : "19:00");

                m.setDishName(title);

                // ✅ tags는 diet tags 그대로 내려도 OK (Home에서 label 처리)
                m.setTags(dish != null ? safe(dish.getTags()) : "");

                meals.add(m);
                mealTitles.add(title);
            }

            day.setMeals(meals);

            days.add(day);
        }

        plan.setDays(days);

        // ✅ preview shoppingList: DB 기반
        plan.setShoppingList(
                ShoppingListBuilder.fromDishIds(
                        dedup(pickedDishIds),
                        language,
                        dishIngredientRepository,
                        ingredientTranslationRepository
                )
        );

        return plan;
    }

    private List<Long> dedup(List<Long> ids) {
        if (ids == null) return List.of();
        return new ArrayList<>(new LinkedHashSet<>(ids));
    }

    private String safe(String s) {
        return s == null ? "" : s;
    }
}
