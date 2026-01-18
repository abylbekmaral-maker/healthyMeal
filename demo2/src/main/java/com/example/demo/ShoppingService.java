package com.example.demo;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ShoppingService {

    private final UserRepository userRepository;
    private final WeeklyPlanRepository weeklyPlanRepository;
    private final DailyMealRepository dailyMealRepository;
    private final DailyMealItemRepository dailyMealItemRepository;

    private final DishIngredientRepository dishIngredientRepository;
    private final IngredientTranslationRepository ingredientTranslationRepository;
    private final ShoppingCheckRepository shoppingCheckRepository;

    private final EntityManager em;

    public ShoppingService(
            UserRepository userRepository,
            WeeklyPlanRepository weeklyPlanRepository,
            DailyMealRepository dailyMealRepository,
            DailyMealItemRepository dailyMealItemRepository,
            DishIngredientRepository dishIngredientRepository,
            IngredientTranslationRepository ingredientTranslationRepository,
            ShoppingCheckRepository shoppingCheckRepository,
            EntityManager em
    ) {
        this.userRepository = userRepository;
        this.weeklyPlanRepository = weeklyPlanRepository;
        this.dailyMealRepository = dailyMealRepository;
        this.dailyMealItemRepository = dailyMealItemRepository;
        this.dishIngredientRepository = dishIngredientRepository;
        this.ingredientTranslationRepository = ingredientTranslationRepository;
        this.shoppingCheckRepository = shoppingCheckRepository;
        this.em = em;
    }

    public ShoppingResponseDto getCurrentWeekShopping(Long userId, String lang) {
        LanguageCode language = safeLang(lang);

        User user = userRepository.findById(userId).orElseThrow();
        LocalDate today = LocalDate.now();

        WeeklyPlan plan = weeklyPlanRepository
                .findTopByUserAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByStartDateDesc(user, today, today)
                .orElse(null);

        if (plan == null) {
            ShoppingResponseDto empty = new ShoppingResponseDto();
            empty.setWeeklyPlanId(null);
            empty.setGroups(List.of());
            return empty;
        }

        // 1) collect dishIds from this weekly plan
        List<DailyMeal> days = dailyMealRepository.findByWeeklyPlanOrderByDate(plan);

        Set<Long> dishIdsSet = new LinkedHashSet<>();
        for (DailyMeal dm : days) {
            List<DailyMealItem> items = dailyMealItemRepository.findByDailyMealOrderById(dm);
            for (DailyMealItem it : items) {
                if (it.getDish() != null && it.getDish().getId() != null) {
                    dishIdsSet.add(it.getDish().getId());
                }
            }
        }

        if (dishIdsSet.isEmpty()) {
            ShoppingResponseDto empty = new ShoppingResponseDto();
            empty.setWeeklyPlanId(plan.getId());
            empty.setGroups(List.of());
            return empty;
        }

        List<Long> dishIds = new ArrayList<>(dishIdsSet);

        // 2) load dish_ingredients
        List<DishIngredient> dis = dishIngredientRepository.findByDish_IdIn(dishIds);

        // 3) existing checks (ingredientId -> checked)
        Map<Long, Boolean> checkedMap = shoppingCheckRepository.findByWeeklyPlan_Id(plan.getId())
                .stream()
                .collect(Collectors.toMap(
                        sc -> sc.getIngredient().getId(),
                        ShoppingCheck::isChecked,
                        (a, b) -> a, // just in case duplicates
                        LinkedHashMap::new
                ));

        // 4) aggregate by (ingredientId + unit)
        record Key(Long ingredientId, String unit) {}
        Map<Key, BigDecimal> sum = new LinkedHashMap<>();
        Map<Long, Ingredient> ingById = new HashMap<>();

        for (DishIngredient di : dis) {
            Ingredient ing = di.getIngredient();
            if (ing == null || ing.getId() == null) continue;

            ingById.putIfAbsent(ing.getId(), ing);

            String unit = di.getUnit() == null ? "" : di.getUnit().trim();
            BigDecimal amount = di.getAmount() == null ? BigDecimal.ZERO : di.getAmount();

            Key key = new Key(ing.getId(), unit);
            sum.put(key, sum.getOrDefault(key, BigDecimal.ZERO).add(amount));
        }

        // 5) group by category (translated label)
        Map<String, List<ShoppingResponseDto.ItemDto>> byCatLabel = new LinkedHashMap<>();

        for (var e : sum.entrySet()) {
            Long ingId = e.getKey().ingredientId();
            Ingredient ing = ingById.get(ingId);
            if (ing == null) continue;

            String catKey = (ing.getCategory() == null || ((String) ing.getCategory()).isBlank())
                    ? "ETC"
                    : ((String) ing.getCategory()).trim().toUpperCase();

            String categoryLabel = categoryLabel(catKey, language);

            // ✅ translate ingredient name by (ingredientId + language)
            String name = ingredientTranslationRepository
                    .findByIngredient_IdAndLanguage(ingId, language)
                    .map(IngredientTranslation::getName)
                    .filter(s -> s != null && !s.isBlank())
                    .orElse("ingredient#" + ingId);

            ShoppingResponseDto.ItemDto item = new ShoppingResponseDto.ItemDto();
            item.setIngredientId(ingId);
            item.setName(name);
            item.setAmount(e.getValue());
            item.setUnit(e.getKey().unit());
            item.setChecked(Boolean.TRUE.equals(checkedMap.get(ingId)));

            byCatLabel.computeIfAbsent(categoryLabel, k -> new ArrayList<>()).add(item);
        }

        // 6) optional: keep stable category order
        List<String> order = categoryOrder(language);
        List<ShoppingResponseDto.GroupDto> groups = new ArrayList<>();

        // put ordered first
        for (String label : order) {
            if (!byCatLabel.containsKey(label)) continue;
            ShoppingResponseDto.GroupDto g = new ShoppingResponseDto.GroupDto();
            g.setCategory(label);
            g.setItems(byCatLabel.get(label));
            groups.add(g);
        }
        // put any unexpected categories last
        for (var e : byCatLabel.entrySet()) {
            if (order.contains(e.getKey())) continue;
            ShoppingResponseDto.GroupDto g = new ShoppingResponseDto.GroupDto();
            g.setCategory(e.getKey());
            g.setItems(e.getValue());
            groups.add(g);
        }

        ShoppingResponseDto out = new ShoppingResponseDto();
        out.setWeeklyPlanId(plan.getId());
        out.setGroups(groups);
        return out;
    }

    @Transactional
    public boolean toggleCheck(Long weeklyPlanId, Long ingredientId) {
        ShoppingCheck sc = shoppingCheckRepository
                .findByWeeklyPlan_IdAndIngredient_Id(weeklyPlanId, ingredientId)
                .orElse(null);

        if (sc == null) {
            // ✅ No reflection. Use JPA reference proxies.
            WeeklyPlan planRef = em.getReference(WeeklyPlan.class, weeklyPlanId);
            Ingredient ingRef = em.getReference(Ingredient.class, ingredientId);

            sc = new ShoppingCheck();
            sc.setWeeklyPlan(planRef);
            sc.setIngredient(ingRef);
            sc.setChecked(true);
            sc.setCheckedAt(LocalDateTime.now());
            shoppingCheckRepository.save(sc);
            return true;
        } else {
            boolean next = !sc.isChecked();
            sc.setChecked(next);
            sc.setCheckedAt(LocalDateTime.now());
            shoppingCheckRepository.save(sc);
            return next;
        }
    }

    // -----------------------
    // helpers
    // -----------------------

    private LanguageCode safeLang(String lang) {
        if (lang == null) return LanguageCode.KO;
        String v = lang.trim().toUpperCase();
        if (v.equals("KO") || v.equals("KR") || v.equals("KOREAN")) return LanguageCode.KO;
        if (v.equals("RU")) return LanguageCode.RU;
        if (v.equals("EN")) return LanguageCode.EN;
        try { return LanguageCode.valueOf(v); }
        catch (Exception e) { return LanguageCode.KO; }
    }

    private String categoryLabel(String key, LanguageCode language) {
        return switch (language) {
            case RU -> switch (key) {
                case "PROTEIN" -> "Белок";
                case "VEG" -> "Овощи";
                case "CARB" -> "Углеводы";
                case "DAIRY" -> "Молочные";
                default -> "Другое";
            };
            case EN -> switch (key) {
                case "PROTEIN" -> "Protein";
                case "VEG" -> "Vegetables";
                case "CARB" -> "Carbs";
                case "DAIRY" -> "Dairy";
                default -> "Other";
            };
            default -> switch (key) {
                case "PROTEIN" -> "단백질";
                case "VEG" -> "채소";
                case "CARB" -> "탄수화물";
                case "DAIRY" -> "유제품";
                default -> "기타";
            };
        };
    }

    private List<String> categoryOrder(LanguageCode language) {
        return switch (language) {
            case RU -> List.of("Белок", "Овощи", "Углеводы", "Молочные", "Другое");
            case EN -> List.of("Protein", "Vegetables", "Carbs", "Dairy", "Other");
            default -> List.of("단백질", "채소", "탄수화물", "유제품", "기타");
        };
    }
}
