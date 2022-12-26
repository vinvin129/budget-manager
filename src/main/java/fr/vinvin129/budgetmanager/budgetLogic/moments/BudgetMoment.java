package fr.vinvin129.budgetmanager.budgetLogic.moments;

import java.util.Arrays;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BudgetMoment that)) return false;

        if (Double.compare(that.allocationPerMonth, allocationPerMonth) != 0) return false;
        if (Double.compare(that.balance, balance) != 0) return false;
        if (!name.equals(that.name)) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(categoryMoments, that.categoryMoments);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = name.hashCode();
        temp = Double.doubleToLongBits(allocationPerMonth);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(balance);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + Arrays.hashCode(categoryMoments);
        return result;
    }
}
