package com.example.demo;

import java.time.LocalDate;
import java.util.List;

public class WeeklyPlanSummaryDto {
    private LocalDate startDate;
    private LocalDate endDate;
    private List<DaySummaryDto> days;
    private List<ShoppingListDto> shoppingList;

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public List<DaySummaryDto> getDays() { return days; }
    public void setDays(List<DaySummaryDto> days) { this.days = days; }

    public List<ShoppingListDto> getShoppingList() { return shoppingList; }
    public void setShoppingList(List<ShoppingListDto> shoppingList) { this.shoppingList = shoppingList; }
}
