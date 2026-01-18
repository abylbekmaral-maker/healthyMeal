package com.example.demo;

import jakarta.persistence.*;

@Entity
@Table(name = "daily_meal_items")
public class DailyMealItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "daily_meal_id")
    private DailyMeal dailyMeal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dish_id")
    private Dish dish;

    @Enumerated(EnumType.STRING)
    @Column(name = "meal_type")
    private MealType mealType;

    @Column(name = "sort_order")
    private Integer sortOrder;

    @Column(name = "note")
    private String note;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public DailyMeal getDailyMeal() { return dailyMeal; }
    public void setDailyMeal(DailyMeal dailyMeal) { this.dailyMeal = dailyMeal; }

    public Dish getDish() { return dish; }
    public void setDish(Dish dish) { this.dish = dish; }

    public MealType getMealType() { return mealType; }
    public void setMealType(MealType mealType) { this.mealType = mealType; }

    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}
