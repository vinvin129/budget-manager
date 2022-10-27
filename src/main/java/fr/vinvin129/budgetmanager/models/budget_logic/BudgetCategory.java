package fr.vinvin129.budgetmanager.models.budget_logic;

import fr.vinvin129.budgetmanager.Spent;
import fr.vinvin129.budgetmanager.exceptions.BudgetCategoryTooSmallException;
import fr.vinvin129.budgetmanager.exceptions.BudgetNotContainCategoryException;
import fr.vinvin129.budgetmanager.exceptions.BudgetTooSmallException;

import java.util.Arrays;

/**
 * a {@link Category} with a linked {@link Budget}
 * @author vinvin129
 */
public class BudgetCategory extends Category{
    /**
     * this linked {@link Budget} object
     */
    private final Budget budget;

    /**
     * create a {@link BudgetCategory} object with a {@link Budget}
     * @param budget the {@link Budget} object
     */
    public BudgetCategory(Budget budget) {
        super(budget.getName());
        this.budget = budget;
    }

    @Override
    void setAllocationPerMonth(double allocationPerMonth) throws BudgetCategoryTooSmallException {
        try {
            this.budget.setAllocationPerMonth(allocationPerMonth);
        } catch (BudgetTooSmallException e) {
            throw new BudgetCategoryTooSmallException();
        }
    }

    /**
     * get the linked {@link Budget}
     * @return a {@link Budget} object
     */
    public Budget getBudget() {
        return budget;
    }

    @Override
    public double getAllocationPerMonth() {
        return this.budget.getAllocationPerMonth();
    }

    @Override
    public double getBalance() {
        return this.budget.getBalance();
    }

    @Override
    void newMonth() {
        this.budget.newMonth();
    }

    @Override
    void addSpent(Spent spent) {
        try {
            this.budget.addSpent(spent);
        } catch (BudgetNotContainCategoryException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * not used in this implementation
     * @return null
     */
    @Override
    public Spent[] getSpentList() {
        return null;
    }

    @Override
    public String toString() {
        return "Catégorie budget " + super.toString();
    }

    @Override
    public double getAmountSpent() {
        return Arrays.stream(this.budget.getCategories()).mapToDouble(Category::getAmountSpent).sum();
    }
}
