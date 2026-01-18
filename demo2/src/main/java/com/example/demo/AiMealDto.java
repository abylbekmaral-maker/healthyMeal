package com.example.demo;

public class AiMealDto {
    private String mealType;     // BREAKFAST/LUNCH/DINNER/SNACK
    private String time;         // "08:00"
    private String dishName;     // Dish.name
    private Integer kcal;
    private Integer protein;     // g
    private String tags;         // "단백질,채소" 이런식
    private String whyGood;      // 한줄

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
