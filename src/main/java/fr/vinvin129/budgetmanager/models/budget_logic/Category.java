package fr.vinvin129.budgetmanager.models.budget_logic;

import fr.vinvin129.budgetmanager.Spent;
import fr.vinvin129.budgetmanager.exceptions.BudgetCategoryTooSmallException;

public abstract class Category {
    private final String name;

    public Category(String name) {
        this.name = name;
    }

    public abstract int getAllocationPerMonth();

    public String getName() {
        return name;
    }

    public abstract int getBalance();

    abstract void setAllocationPerMonth(int allocationPerMonth) throws BudgetCategoryTooSmallException;

    abstract void newMonth();

    abstract void addSpent(Spent spent);

    public abstract Spent[] getSpentList();
}
