package com.example.demo;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class HomeService {

    private final DailyMealRepository dailyMealRepository;
    private final DailyMealItemRepository dailyMealItemRepository;
    private final UserRepository userRepository;
    private final WeeklyPlanRepository weeklyPlanRepository;
    private final DishTranslationRepository dishTranslationRepository;

    public HomeService(
            DailyMealRepository dailyMealRepository,
            DailyMealItemRepository dailyMealItemRepository,
            UserRepository userRepository,
            WeeklyPlanRepository weeklyPlanRepository,
            DishTranslationRepository dishTranslationRepository
    ) {
        this.dailyMealRepository = dailyMealRepository;
        this.dailyMealItemRepository = dailyMealItemRepository;
        this.userRepository = userRepository;
        this.weeklyPlanRepository = weeklyPlanRepository;
        this.dishTranslationRepository = dishTranslationRepository;
    }

    public HomeTodayDto getToday(Long userId, LanguageCode language) {
        return getByDate(userId, LocalDate.now(), language);
    }

    public HomeTodayDto getByDate(Long userId, LocalDate date, LanguageCode language) {
        User user = userRepository.findById(userId).orElseThrow();

        WeeklyPlan plan = weeklyPlanRepository
                .findTopByUserAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByStartDateDesc(
                        user, date, date
                )
                .orElse(null);

        if (plan == null) {
            return emptyDto(date, language,
                    "오늘의 식단이 없어요.\nPlanner에서 주간 플랜을 생성해주세요.",
                    "Сегодня нет рациона.\nСоздайте недельный план в Planner.",
                    "No meals for today.\nCreate a weekly plan in Planner."
            );
        }

        DailyMeal dailyMeal = dailyMealRepository
                .findByWeeklyPlanAndDate(plan, date)
                .orElse(null);

        if (dailyMeal == null) {
            return emptyDto(date, language,
                    "해당 날짜 식단이 없습니다.",
                    "На эту дату нет рациона.",
                    "No meals for this date."
            );
        }

        List<DailyMealItem> items = dailyMealItemRepository.findByDailyMealOrderById(dailyMeal);

        HomeTodayDto dto = new HomeTodayDto();
        dto.setActive(true);
        dto.setDate(date);

        int totalKcal = 0;
        int totalProtein = 0;
        int totalFiber = 0;

        List<HomeTodayDto.HomeMealItemDto> meals = new ArrayList<>();

        for (DailyMealItem it : items) {
            Dish dish = it.getDish();

            DishTranslation tr = null;
            if (dish != null) {
                tr = dishTranslationRepository
                        .findByDish_IdAndLanguage(dish.getId(), language)
                        .orElse(null);
            }

            String title = (tr != null && notBlank(tr.getName()))
                    ? tr.getName()
                    : defaultTitle(language);

            String whyGood = (tr != null && tr.getDescription() != null) ? tr.getDescription() : "";

            int kcal = (dish != null && dish.getKcal() != null) ? dish.getKcal() : 0;

            int protein = 0;
            if (dish != null && dish.getProteinG() != null) protein = dish.getProteinG().intValue();

            int fiber = 0;
            if (dish != null && dish.getFiberG() != null) fiber = dish.getFiberG().intValue();

            HomeTodayDto.HomeMealItemDto m = new HomeTodayDto.HomeMealItemDto();
            m.setTime(safe(it.getNote()));
            m.setMealType(it.getMealType() != null ? it.getMealType().name() : "");

            m.setTitle(title);
            m.setCalories(kcal);
            m.setProtein(protein);
            m.setFiber(fiber);
            m.setWhyGood(whyGood);

            // ✅ tags는 원본 그대로 내려주기 (프론트에서 KO/RU/EN 라벨링)
            m.setTags(dish != null ? safe(dish.getTags()) : "");

            meals.add(m);

            totalKcal += kcal;
            totalProtein += protein;
            totalFiber += fiber;
        }

        dto.setMeals(meals);
        dto.setMealCount(items.size());
        dto.setTotalCalories(totalKcal);
        dto.setTotalProtein(totalProtein);
        dto.setTotalFiber(totalFiber);

        return dto;
    }

    private HomeTodayDto emptyDto(LocalDate date, LanguageCode language, String ko, String ru, String en) {
        HomeTodayDto dto = new HomeTodayDto();
        dto.setActive(false);
        dto.setDate(date);
        dto.setMeals(List.of());
        dto.setMealCount(0);
        dto.setTotalCalories(0);
        dto.setTotalProtein(0);
        dto.setTotalFiber(0);

        dto.setMessage(switch (language) {
            case RU -> ru;
            case EN -> en;
            default -> ko;
        });

        return dto;
    }

    private String defaultTitle(LanguageCode language) {
        return switch (language) {
            case RU -> "Рекомендуемое блюдо";
            case EN -> "Recommended meal";
            default -> "추천 메뉴";
        };
    }

    private String safe(String s) {
        return s == null ? "" : s;
    }

    private boolean notBlank(String s) {
        return s != null && !s.isBlank();
    }
}
