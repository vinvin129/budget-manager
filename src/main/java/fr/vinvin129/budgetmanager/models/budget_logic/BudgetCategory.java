package fr.vinvin129.budgetmanager.models.budget_logic;

import fr.vinvin129.budgetmanager.Spent;
import fr.vinvin129.budgetmanager.exceptions.BudgetCategoryTooSmallException;
import fr.vinvin129.budgetmanager.exceptions.BudgetNotContainCategoryException;
import fr.vinvin129.budgetmanager.exceptions.BudgetTooSmallException;

public class BudgetCategory extends Category{
    private final Budget budget;

    public BudgetCategory(Budget budget) {
        super(budget.getName());
        this.budget = budget;
    }

    @Override
    void setAllocationPerMonth(int allocationPerMonth) throws BudgetCategoryTooSmallException {
        try {
            this.budget.setAllocationPerMonth(allocationPerMonth);
        } catch (BudgetTooSmallException e) {
            throw new BudgetCategoryTooSmallException();
        }
    }

    public Budget getBudget() {
        return budget;
    }

    @Override
    public int getAllocationPerMonth() {
        return this.budget.getAllocationPerMonth();
    }

    @Override
    public int getBalance() {
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

    @Override
    public Spent[] getSpentList() {
        return null;
    }
}
