package com.example.demo;

import java.time.LocalDate;
import java.util.List;

public class PlannerPlanDto {
    private LocalDate startDate;
    private LocalDate endDate;
    private List<PlannerDayDto> days;
    private List<ShoppingListDto> shoppingList;

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public List<PlannerDayDto> getDays() { return days; }
    public void setDays(List<PlannerDayDto> days) { this.days = days; }

    public List<ShoppingListDto> getShoppingList() { return shoppingList; }
    public void setShoppingList(List<ShoppingListDto> shoppingList) { this.shoppingList = shoppingList; }

    public static class PlannerDayDto {
        private LocalDate date;
        private List<AiMealDto> meals;

        public LocalDate getDate() { return date; }
        public void setDate(LocalDate date) { this.date = date; }

        public List<AiMealDto> getMeals() { return meals; }
        public void setMeals(List<AiMealDto> meals) { this.meals = meals; }
    }
}
