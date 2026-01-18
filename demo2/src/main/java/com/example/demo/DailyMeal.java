package com.example.demo;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "daily_meals")
public class DailyMeal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "weekly_plan_id")
    private WeeklyPlan weeklyPlan;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "day_index")
    private Integer dayIndex;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public WeeklyPlan getWeeklyPlan() { return weeklyPlan; }
    public void setWeeklyPlan(WeeklyPlan weeklyPlan) { this.weeklyPlan = weeklyPlan; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public Integer getDayIndex() { return dayIndex; }
    public void setDayIndex(Integer dayIndex) { this.dayIndex = dayIndex; }
}
