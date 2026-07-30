package com.example.demo.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "dishes")
public class Dish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private MealType mealType;

    private Integer kcal;

    @Column(name = "protein_g")
    private BigDecimal proteinG;

    @Column(name = "carbs_g")
    private BigDecimal carbsG;

    @Column(name = "fat_g")
    private BigDecimal fatG;
    
    @Column(name = "fiber_g")
    private BigDecimal fiberG;
    
    private String tags;

    private String allergens;

    @Column(name = "is_active")
    private boolean active = true;
    

    public Long getId() { return id; }

    public MealType getMealType() { return mealType; }
    public void setMealType(MealType mealType) { this.mealType = mealType; }

    public Integer getKcal() { return kcal; }
    public void setKcal(Integer kcal) { this.kcal = kcal; }

    public BigDecimal getProteinG() { return proteinG; }
    public void setProteinG(BigDecimal proteinG) { this.proteinG = proteinG; }

    public BigDecimal getCarbsG() { return carbsG; }
    public void setCarbsG(BigDecimal carbsG) { this.carbsG = carbsG; }

    public BigDecimal getFatG() { return fatG; }
    public void setFatG(BigDecimal fatG) { this.fatG = fatG; }

    public BigDecimal getFiberG() { return fiberG; }
    public void setFiberG(BigDecimal fiberG) { this.fiberG = fiberG; }

    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }

    public String getAllergens() { return allergens; }
    public void setAllergens(String allergens) { this.allergens = allergens; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
// @Data로 나중에 바뀔게요