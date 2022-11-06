package fr.vinvin129.budgetmanager.budgetLogic.moments;

public record BudgetMoment(String name, double allocationPerMonth, double balance, CategoryMoment[] categoryMoments) {
}
