package com.example.demo.dto;

public class AiMealDto {
    private String mealType;     // BREAKFAST/LUNCH/DINNER/SNACK
    private String time;
    private String dishName;     // Dish.nam
    private Integer kcal;
    private Integer protein;
    private String tags;
    private String whyGood;

    public String getMealType() { return mealType; }
    public void setMealType(String mealType) { this.mealType = mealType; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    public String getDishName() { return dishName; }
    public void setDishName(String dishName) { this.dishName = dishName; }

    public Integer getKcal() { return kcal; }
    public void setKcal(Integer kcal) { this.kcal = kcal; }

    public Integer getProtein() { return protein; }
    public void setProtein(Integer protein) { this.protein = protein; }

    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }

    public String getWhyGood() { return whyGood; }
    public void setWhyGood(String whyGood) { this.whyGood = whyGood; }
}
