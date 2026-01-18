package com.example.demo;

import java.time.LocalDate;
import java.util.List;

public class AiDayPlanDto {
    private LocalDate date;
    private List<AiMealDto> meals;

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public List<AiMealDto> getMeals() { return meals; }
    public void setMeals(List<AiMealDto> meals) { this.meals = meals; }
}
