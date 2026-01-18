package com.example.demo;

import java.time.LocalDate;
import java.util.List;

public class HomeTodayDto {

    private LocalDate date;
    private int mealCount;
    private int totalCalories;
    private int totalProtein;
    private int totalFiber;

    private List<HomeMealItemDto> meals;

    private boolean active;
    private String message;

    // ===== getters / setters =====
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public int getMealCount() { return mealCount; }
    public void setMealCount(int mealCount) { this.mealCount = mealCount; }

    public int getTotalCalories() { return totalCalories; }
    public void setTotalCalories(int totalCalories) { this.totalCalories = totalCalories; }

    public int getTotalProtein() { return totalProtein; }
    public void setTotalProtein(int totalProtein) { this.totalProtein = totalProtein; }

    public int getTotalFiber() { return totalFiber; }
    public void setTotalFiber(int totalFiber) { this.totalFiber = totalFiber; }

    public List<HomeMealItemDto> getMeals() { return meals; }
    public void setMeals(List<HomeMealItemDto> meals) { this.meals = meals; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    // ===== inner DTO =====
    public static class HomeMealItemDto {
        private String time;
        private String title;
        private int calories;
        private int protein;
        private int fiber;
        private String tags;
        private String whyGood;
        private String mealType;

        public String getTime() { return time; }
        public void setTime(String time) { this.time = time; }

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }

        public int getCalories() { return calories; }
        public void setCalories(int calories) { this.calories = calories; }

        public int getProtein() { return protein; }
        public void setProtein(int protein) { this.protein = protein; }

        public int getFiber() { return fiber; }
        public void setFiber(int fiber) { this.fiber = fiber; }

        public String getTags() { return tags; }
        public void setTags(String tags) { this.tags = tags; }

        public String getWhyGood() { return whyGood; }
        public void setWhyGood(String whyGood) { this.whyGood = whyGood; }

        public String getMealType() { return mealType; }
        public void setMealType(String mealType) { this.mealType = mealType; }
    }
}
