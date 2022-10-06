package fr.vinvin129.budgetmanager.models.budget_logic;

import fr.vinvin129.budgetmanager.Spent;
import fr.vinvin129.budgetmanager.exceptions.BudgetCategoryTooSmallException;
import fr.vinvin129.budgetmanager.exceptions.BudgetNotContainCategoryException;
import fr.vinvin129.budgetmanager.exceptions.BudgetTooSmallException;
import fr.vinvin129.budgetmanager.exceptions.CategoryTooBigException;

import java.util.ArrayList;
import java.util.List;

public class Budget {
    private final String name;
    private int allocationPerMonth;
    private int balance = 0;
    private final List<Category> categories = new ArrayList<>();

    public Budget(String name, int allocationPerMonth) {
        this.name = name;
        this.allocationPerMonth = allocationPerMonth;
    }

    public int getAllocationPerMonth() {
        return allocationPerMonth;
    }

    public int getBalance() {
        return balance;
    }

    public String getName() {
        return name;
    }

    public void setAllocationPerMonth(int allocationPerMonth) throws BudgetTooSmallException {
        int totalBudget = 0;
        for (Category category : this.categories) {
            totalBudget += category.getAllocationPerMonth();
        }

        if (totalBudget > allocationPerMonth) {
            throw new BudgetTooSmallException();
        }
        this.allocationPerMonth = allocationPerMonth;
    }

    public void setAllocationPerMonthOfCategory(Category category, int allocationPerMonth) throws BudgetCategoryTooSmallException, CategoryTooBigException {
        int newTotalAllocation = 0;
        for (Category c : this.categories) {
            newTotalAllocation += c != category ? c.getAllocationPerMonth() : allocationPerMonth;
        }

        if (newTotalAllocation > this.allocationPerMonth) {
            throw new CategoryTooBigException();
        }

        category.setAllocationPerMonth(allocationPerMonth);
    }

    public void newMonth() {
        this.categories.forEach(Category::newMonth);
        this.balance += allocationPerMonth;
        for (Category category : categories) {
            if (category instanceof BudgetCategory) {
                this.balance -= category.getAllocationPerMonth();
            }
        }
    }

    public void addCategory(Category category) {
        this.categories.add(category);
    }

    public void removeCategory(Category category) {
        this.categories.remove(category);
    }

    public Category[] getCategories() {
        return this.categories.toArray(new Category[0]);
    }

    public void addSpent(Spent spent) throws BudgetNotContainCategoryException {
        if (!this.categories.contains(spent.category())) {
            throw new BudgetNotContainCategoryException(spent.category());
        }
        this.categories.forEach(category -> {
            if (category == spent.category()) {
                category.addSpent(spent);
                if (category instanceof StandardCategory) {
                    this.balance -= spent.price();
                }
            }
        });
    }
}
