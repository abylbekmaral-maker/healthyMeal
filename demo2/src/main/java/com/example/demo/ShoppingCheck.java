package com.example.demo;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "shopping_checks",
       uniqueConstraints = @UniqueConstraint(columnNames = {"weekly_plan_id", "ingredient_id"}))
public class ShoppingCheck {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "weekly_plan_id", nullable = false)
    private WeeklyPlan weeklyPlan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_id", nullable = false)
    private Ingredient ingredient;

    @Column(nullable = false)
    private boolean checked = false;

    @Column(name = "checked_at")
    private LocalDateTime checkedAt;

    public Long getId() { return id; }
    public WeeklyPlan getWeeklyPlan() { return weeklyPlan; }
    public void setWeeklyPlan(WeeklyPlan weeklyPlan) { this.weeklyPlan = weeklyPlan; }
    public Ingredient getIngredient() { return ingredient; }
    public void setIngredient(Ingredient ingredient) { this.ingredient = ingredient; }
    public boolean isChecked() { return checked; }
    public void setChecked(boolean checked) { this.checked = checked; }
    public LocalDateTime getCheckedAt() { return checkedAt; }
    public void setCheckedAt(LocalDateTime checkedAt) { this.checkedAt = checkedAt; }
}
