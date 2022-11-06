package fr.vinvin129.budgetmanager.budgetLogic.moments;


import fr.vinvin129.budgetmanager.budgetLogic.Spent;

public record CategoryMoment(String name, double allocationPerMonth, double balance, Spent[] expenses, BudgetMoment budgetMoment) {
}
