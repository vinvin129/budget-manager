package fr.vinvin129.budgetmanager.budgetLogic.moments;


import fr.vinvin129.budgetmanager.budgetLogic.Spent;

/**
 * an immutable model for a category
 * @param name this name
 * @param allocationPerMonth this allocation per month
 * @param balance this balance
 * @param expenses this {@link Spent} list
 * @param budgetMoment a {@link BudgetMoment} object if is a budget category or null if is a standard category
 */
public record CategoryMoment(
        String name,
        double allocationPerMonth,
        double balance,
        Spent[] expenses,
        BudgetMoment budgetMoment) {
}
