package com.example.demo;

import java.time.LocalDate;
import java.util.List;

public class AiWeeklyPlanDto {
    public List<DayPlan> days;

    public static class DayPlan {
        public String date;
        public List<Meal> meals;
    }

    public static class Meal {
        public String mealType;
        public String time;
        public String dishName;
        public int kcal;
        public int protein;
        public String tags;
        public String whyGood;
    }

	public LocalDate getStartDate() {
		// TODO Auto-generated method stub
		return null;
	}
}
