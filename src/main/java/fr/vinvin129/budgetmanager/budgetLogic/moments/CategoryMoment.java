package fr.vinvin129.budgetmanager.budgetLogic.moments;


import fr.vinvin129.budgetmanager.budgetLogic.Spent;

import java.util.Arrays;
import java.util.Objects;

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
    public static CategoryMoment create(String name, double allocationPerMonth) {
        return new CategoryMoment(
                name,
                allocationPerMonth,
                0,
                new Spent[]{},
                null);
    }

    public static CategoryMoment create(BudgetMoment budgetMoment) {
        return new CategoryMoment(
                budgetMoment.name(),
                budgetMoment.allocationPerMonth(),
                budgetMoment.balance(),
                new Spent[]{},
                budgetMoment);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CategoryMoment that)) return false;

        if (Double.compare(that.allocationPerMonth, allocationPerMonth) != 0) return false;
        if (Double.compare(that.balance, balance) != 0) return false;
        if (!name.equals(that.name)) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(expenses, that.expenses)) return false;
        return Objects.equals(budgetMoment, that.budgetMoment);
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
        result = 31 * result + Arrays.hashCode(expenses);
        result = 31 * result + (budgetMoment != null ? budgetMoment.hashCode() : 0);
        return result;
    }
}
