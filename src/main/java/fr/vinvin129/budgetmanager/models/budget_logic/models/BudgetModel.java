package fr.vinvin129.budgetmanager.models.budget_logic.models;

import fr.vinvin129.budgetmanager.models.budget_logic.Category;

import java.io.Serializable;

public class BudgetModel implements Serializable {
    /**
     * the name of Budget
     */
    private String name;
    /**
     * money added in the balance each month
     */
    private double allocationPerMonth;
    /**
     * total available in the budget
     */
    private double balance;
    /**
     * {@link Category} List of this Budget
     */
    private CategoryModel[] categories;

    public BudgetModel(String name, double allocationPerMonth, double balance, CategoryModel[] categories) {
        this.name = name;
        this.allocationPerMonth = allocationPerMonth;
        this.balance = balance;
        this.categories = categories;
    }

    /**
     * get the name of budget
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * get the value of allocation for each month for Budget
     * @return the value
     */
    public double getAllocationPerMonth() {
        return allocationPerMonth;
    }

    /**
     * get the balance of budget
     * @return the balance
     */
    public double getBalance() {
        return balance;
    }

    /**
     * get {@link CategoryModel} list of categories in this budget
     * @return {@link CategoryModel} list
     */
    public CategoryModel[] getCategories() {
        return categories;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAllocationPerMonth(double allocationPerMonth) {
        this.allocationPerMonth = allocationPerMonth;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void setCategories(CategoryModel[] categories) {
        this.categories = categories;
    }
}
