package fr.vinvin129.budgetmanager.budgetLogic.moments;

/**
 * an immutable model for a budget
 * @param name the budget name
 * @param allocationPerMonth the budget allocation per month
 * @param balance the budget balance
 * @param categoryMoments list of {@link CategoryMoment} for categories in this budget
 * @author vinvin129
 */
public record BudgetMoment(
        String name,
        double allocationPerMonth,
        double balance,
        CategoryMoment[] categoryMoments) {
    public static BudgetMoment create(String name, double allocationPerMonth) {
        return new BudgetMoment(name, allocationPerMonth, 0, new CategoryMoment[]{});
    }
}
