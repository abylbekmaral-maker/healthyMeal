package com.example.demo;

public class ProfileSetupForm {

    private Integer userId;       // 반드시 들어와야 되는 값

    private Integer heightCm;     // 키
    private Double weightKg;      // 몸무게
    private String activityLevel; // "0"~"4"
    private String goal;          // "loss", "keep", "gain"
    private String notes;         // 식이 제한
    private String conditions;    // 건강 상태 (콤마 문자열)

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

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String getConditions() {
        return conditions;
    }

    public void setConditions(String conditions) {
        this.conditions = conditions;
    }
}
