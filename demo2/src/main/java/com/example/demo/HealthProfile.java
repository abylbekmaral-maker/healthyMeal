package com.example.demo;

import jakarta.persistence.*;

@Entity
@Table(name = "user_profile")
public class HealthProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "height_cm")
    private Integer heightCm;

    @Column(name = "weight_kg")
    private Double weightKg;

    @Column(name = "activity_level")
    private String activityLevel;   // "0" ~ "4"

    @Column(name = "goal")
    private String goal;            // "loss", "keep", "gain"

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public Integer getHeightCm() { return heightCm; }
    public void setHeightCm(Integer heightCm) { this.heightCm = heightCm; }

    public Double getWeightKg() { return weightKg; }
    public void setWeightKg(Double weightKg) { this.weightKg = weightKg; }

    public String getActivityLevel() { return activityLevel; }
    public void setActivityLevel(String activityLevel) { this.activityLevel = activityLevel; }

    public String getGoal() { return goal; }
    public void setGoal(String goal) { this.goal = goal; }

    @Column(name = "notes")
    private String notes;   // 식이 제한(gluten,sugar,...)

   
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    @Column(name = "conditions")
    private String conditions;

    public String getConditions() { return conditions; }
    public void setConditions(String conditions) { this.conditions = conditions; }


   
}

