package com.example.demo;

import java.math.BigDecimal;
import java.util.List;

public class ShoppingResponseDto {
    private Long weeklyPlanId;
    private List<GroupDto> groups;

    public Long getWeeklyPlanId() { return weeklyPlanId; }
    public void setWeeklyPlanId(Long weeklyPlanId) { this.weeklyPlanId = weeklyPlanId; }
    public List<GroupDto> getGroups() { return groups; }
    public void setGroups(List<GroupDto> groups) { this.groups = groups; }

    public static class GroupDto {
        private String category;
        private List<ItemDto> items;

        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
        public List<ItemDto> getItems() { return items; }
        public void setItems(List<ItemDto> items) { this.items = items; }
    }

    public static class ItemDto {
        private Long ingredientId;
        private String name;       // translated
        private BigDecimal amount; // summed
        private String unit;
        private boolean checked;

        public Long getIngredientId() { return ingredientId; }
        public void setIngredientId(Long ingredientId) { this.ingredientId = ingredientId; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public BigDecimal getAmount() { return amount; }
        public void setAmount(BigDecimal amount) { this.amount = amount; }
        public String getUnit() { return unit; }
        public void setUnit(String unit) { this.unit = unit; }
        public boolean isChecked() { return checked; }
        public void setChecked(boolean checked) { this.checked = checked; }
    }
}
