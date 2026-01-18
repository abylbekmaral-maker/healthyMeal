package com.example.demo;

import java.time.LocalDate;
import java.util.List;

public class DaySummaryDto {
    private LocalDate date;
    private List<String> mealTitles;

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public List<String> getMealTitles() { return mealTitles; }
    public void setMealTitles(List<String> mealTitles) { this.mealTitles = mealTitles; }
}
