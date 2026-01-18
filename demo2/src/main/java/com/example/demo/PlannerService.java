package com.example.demo;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Service
public class PlannerService {

    private final MealPlanGeneratorService generator;

    private final UserRepository userRepository;
    private final WeeklyPlanRepository weeklyPlanRepository;
    private final DailyMealRepository dailyMealRepository;
    private final DailyMealItemRepository dailyMealItemRepository;

    private final DishTranslationRepository dishTranslationRepository;
    private final DishRepository dishRepository;

    private final DishIngredientRepository dishIngredientRepository;
    private final IngredientTranslationRepository ingredientTranslationRepository;

    public PlannerService(
            MealPlanGeneratorService generator,
            UserRepository userRepository,
            WeeklyPlanRepository weeklyPlanRepository,
            DailyMealRepository dailyMealRepository,
            DailyMealItemRepository dailyMealItemRepository,
            DishTranslationRepository dishTranslationRepository,
            DishRepository dishRepository,
            DishIngredientRepository dishIngredientRepository,
            IngredientTranslationRepository ingredientTranslationRepository
    ) {
        this.generator = generator;
        this.userRepository = userRepository;
        this.weeklyPlanRepository = weeklyPlanRepository;
        this.dailyMealRepository = dailyMealRepository;
        this.dailyMealItemRepository = dailyMealItemRepository;
        this.dishTranslationRepository = dishTranslationRepository;
        this.dishRepository = dishRepository;
        this.dishIngredientRepository = dishIngredientRepository;
        this.ingredientTranslationRepository = ingredientTranslationRepository;
    }

    public PlannerPlanDto generatePreview(Long userId, LanguageCode language) {
        return generator.generatePreview(userId, language);
    }

    public WeeklyPlanSummaryDto getCurrentPlan(Long userId, LanguageCode language) {
        User user = userRepository.findById(userId).orElseThrow();
        LocalDate today = LocalDate.now();

        WeeklyPlan plan = weeklyPlanRepository
                .findTopByUserAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByStartDateDesc(
                        user, today, today
                )
                .orElse(null);

        if (plan == null) {
            WeeklyPlanSummaryDto empty = new WeeklyPlanSummaryDto();
            empty.setStartDate(today);
            empty.setEndDate(today.plusDays(6));
            empty.setDays(List.of());
            empty.setShoppingList(List.of());
            return empty;
        }

        return buildSummaryDto(plan, language);
    }

    @Transactional
    public void deleteCurrentPlan(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        LocalDate today = LocalDate.now();

        WeeklyPlan current = weeklyPlanRepository
                .findTopByUserAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByStartDateDesc(
                        user, today, today
                )
                .orElse(null);

        if (current == null) return;

        Long planId = current.getId();
        dailyMealItemRepository.deleteByWeeklyPlanId(planId);
        dailyMealRepository.deleteByWeeklyPlanId(planId);
        weeklyPlanRepository.deleteById(planId);
    }

    @Transactional
    public Long saveWeeklyPlan(Long userId, PlannerPlanDto preview, LanguageCode language) {
        User user = userRepository.findById(userId).orElseThrow();
        deleteCurrentPlan(userId);

        LocalDate start = (preview != null && preview.getStartDate() != null)
                ? preview.getStartDate()
                : LocalDate.now();

        LocalDate end = (preview != null && preview.getEndDate() != null)
                ? preview.getEndDate()
                : start.plusDays(6);

        WeeklyPlan plan = new WeeklyPlan();
        plan.setUser(user);
        plan.setStartDate(start);
        plan.setEndDate(end);

        WeeklyPlan savedPlan = weeklyPlanRepository.save(plan);

        if (preview == null || preview.getDays() == null) {
            return savedPlan.getId();
        }

        for (int i = 0; i < preview.getDays().size(); i++) {
            PlannerPlanDto.PlannerDayDto dayDto = preview.getDays().get(i);

            DailyMeal dm = new DailyMeal();
            dm.setWeeklyPlan(savedPlan);

            LocalDate dayDate = (dayDto != null && dayDto.getDate() != null)
                    ? dayDto.getDate()
                    : start.plusDays(i);

            dm.setDate(dayDate);
            dm.setDayIndex(i);

            DailyMeal savedDm = dailyMealRepository.save(dm);

            if (dayDto == null || dayDto.getMeals() == null) continue;

            for (int j = 0; j < dayDto.getMeals().size(); j++) {
                AiMealDto mealDto = dayDto.getMeals().get(j);
                if (mealDto == null) continue;

                DailyMealItem item = new DailyMealItem();
                item.setDailyMeal(savedDm);

                try {
                    item.setMealType(MealType.valueOf(mealDto.getMealType()));
                } catch (Exception e) {
                    item.setMealType(MealType.BREAKFAST);
                }

                item.setSortOrder(j);
                item.setNote(mealDto.getTime());

                Dish dish = findOrFallbackDish(mealDto.getDishName(), language);
                item.setDish(dish);

                dailyMealItemRepository.save(item);
            }
        }

        return savedPlan.getId();
    }

    // -------------------------
    // 내부 유틸
    // -------------------------

    private Dish findOrFallbackDish(String dishName, LanguageCode language) {
        if (dishName != null && !dishName.isBlank()) {
            Dish dish = dishTranslationRepository
                    .findByNameAndLanguage(dishName.trim(), language)
                    .map(DishTranslation::getDish)
                    .orElse(null);
            if (dish != null) return dish;
        }
        return dishRepository.findAll().stream().findFirst().orElse(null);
    }

    private WeeklyPlanSummaryDto buildSummaryDto(WeeklyPlan plan, LanguageCode language) {
        WeeklyPlanSummaryDto dto = new WeeklyPlanSummaryDto();
        dto.setStartDate(plan.getStartDate());
        dto.setEndDate(plan.getEndDate());

        List<DailyMeal> meals = dailyMealRepository.findByWeeklyPlanOrderByDate(plan);

        List<DaySummaryDto> days = new ArrayList<>();
        for (DailyMeal dm : meals) {
            DaySummaryDto ds = new DaySummaryDto();
            ds.setDate(dm.getDate());

            List<DailyMealItem> items = dailyMealItemRepository.findByDailyMealOrderById(dm);

            List<String> titles = new ArrayList<>();
            for (DailyMealItem it : items) {
                titles.add(resolveDishTitle(it.getDish(), language));
            }
            ds.setMealTitles(titles);
            days.add(ds);
        }

        dto.setDays(days);

        // ✅ 쇼핑리스트: 합산/정리 버전
        dto.setShoppingList(buildShoppingListFromPlan(plan, language));

        return dto;
    }

    private String resolveDishTitle(Dish dish, LanguageCode language) {
        if (dish == null) return "(dish)";

        DishTranslation tr = dishTranslationRepository
                .findByDish_IdAndLanguage(dish.getId(), language)
                .orElse(null);

        if (tr != null && tr.getName() != null && !tr.getName().isBlank()) {
            return tr.getName();
        }
        return "Dish #" + dish.getId();
    }

    private List<ShoppingListDto> buildShoppingListFromPlan(WeeklyPlan plan, LanguageCode language) {
        List<DailyMeal> dms = dailyMealRepository.findByWeeklyPlanOrderByDate(plan);

        Set<Long> dishIdsSet = new LinkedHashSet<>();
        for (DailyMeal dm : dms) {
            List<DailyMealItem> items = dailyMealItemRepository.findByDailyMealOrderById(dm);
            for (DailyMealItem it : items) {
                if (it.getDish() != null && it.getDish().getId() != null) {
                    dishIdsSet.add(it.getDish().getId());
                }
            }
        }
        if (dishIdsSet.isEmpty()) return List.of();

        return ShoppingListBuilder.fromDishIds(
                new ArrayList<>(dishIdsSet),
                language,
                dishIngredientRepository,
                ingredientTranslationRepository
        );
    }
}
