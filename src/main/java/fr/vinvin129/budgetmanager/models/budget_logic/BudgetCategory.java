package fr.vinvin129.budgetmanager.models.budget_logic;

import fr.vinvin129.budgetmanager.Spent;

public class BudgetCategory extends Category{
    private final Budget budget;

    public BudgetCategory(Budget budget) {
        super(budget.getName());
        this.budget = budget;
    }

    public BudgetCategory(String name, int allocationPerMonth) {
        super(name);
        this.budget = new Budget(name, allocationPerMonth);
        this.budget.addCategory(new StandardCategory(name + " catégorie", allocationPerMonth));
    }

    @Override
    public void setAllocationPerMonth(int allocationPerMonth) {
        this.budget.setAllocationPerMonth(allocationPerMonth);
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
        this.budget.addSpent(spent);
    }

    @Override
    public Spent[] getSpentList() {
        return null;
    }
}
