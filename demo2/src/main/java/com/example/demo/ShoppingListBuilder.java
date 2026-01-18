package com.example.demo;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class ShoppingListBuilder {

    private ShoppingListBuilder() {}

    /**
     * ✅ dishIds -> dish_ingredients + ingredient_translations 기반 쇼핑리스트 생성
     * - 같은 ingredientId + unit 은 합산
     * - category는 Ingredient.category(PROTEIN/VEG/...) 기반
     * - 출력은 Planner용: "재료명 100g" 같은 문자열
     */
    public static List<ShoppingListDto> fromDishIds(
            List<Long> dishIds,
            LanguageCode language,
            DishIngredientRepository dishIngredientRepository,
            IngredientTranslationRepository ingredientTranslationRepository
    ) {
        if (dishIds == null || dishIds.isEmpty()) return List.of();

        List<DishIngredient> links = dishIngredientRepository.findByDish_IdIn(dishIds);
        if (links == null || links.isEmpty()) return List.of();

        // aggregate by (ingredientId + unit)
        record Key(Long ingredientId, String unit) {}
        Map<Key, BigDecimal> sum = new LinkedHashMap<>();
        Map<Long, Ingredient> ingById = new HashMap<>();

        for (DishIngredient di : links) {
            if (di == null || di.getIngredient() == null || di.getIngredient().getId() == null) continue;

            Long ingId = di.getIngredient().getId();
            ingById.putIfAbsent(ingId, di.getIngredient());

            String unit = di.getUnit() == null ? "" : di.getUnit().trim();
            BigDecimal amount = di.getAmount() == null ? BigDecimal.ZERO : di.getAmount();

            Key key = new Key(ingId, unit);
            sum.put(key, sum.getOrDefault(key, BigDecimal.ZERO).add(amount));
        }

        // categoryLabel -> items
        Map<String, List<String>> byCat = new LinkedHashMap<>();

        for (var e : sum.entrySet()) {
            Long ingId = e.getKey().ingredientId();
            Ingredient ing = ingById.get(ingId);
            if (ing == null) continue;

            String catKey = (ing.getCategory() == null || ing.getCategory().isBlank())
                    ? "ETC"
                    : ing.getCategory().trim().toUpperCase();

            String catLabel = categoryLabel(catKey, language);

            String name = ingredientTranslationRepository
                    .findByIngredient_IdAndLanguage(ingId, language)
                    .map(IngredientTranslation::getName)
                    .filter(s -> s != null && !s.isBlank())
                    .orElse("Ingredient #" + ingId);

            String line = formatLine(name, e.getValue(), e.getKey().unit());

            byCat.computeIfAbsent(catLabel, k -> new ArrayList<>()).add(line);
        }

        // stable order
        List<String> order = categoryOrder(language);

        List<ShoppingListDto> out = new ArrayList<>();
        for (String label : order) {
            List<String> items = byCat.get(label);
            if (items == null || items.isEmpty()) continue;

            ShoppingListDto dto = new ShoppingListDto();
            dto.setCategory(label);
            dto.setItems(items);
            out.add(dto);
        }
        // any unexpected categories
        for (var e : byCat.entrySet()) {
            if (order.contains(e.getKey())) continue;
            ShoppingListDto dto = new ShoppingListDto();
            dto.setCategory(e.getKey());
            dto.setItems(e.getValue());
            out.add(dto);
        }

        return out;
    }

    private static String formatLine(String name, BigDecimal amount, String unit) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) == 0 || unit == null || unit.isBlank()) {
            return name;
        }
        String a = amount.stripTrailingZeros().toPlainString();
        return name + " " + a + unit;
    }

    private static String categoryLabel(String key, LanguageCode language) {
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

    private static List<String> categoryOrder(LanguageCode language) {
        return switch (language) {
            case RU -> List.of("Белок", "Овощи", "Углеводы", "Молочные", "Другое");
            case EN -> List.of("Protein", "Vegetables", "Carbs", "Dairy", "Other");
            default -> List.of("단백질", "채소", "탄수화물", "유제품", "기타");
        };
    }
}
