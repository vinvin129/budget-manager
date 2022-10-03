package fr.vinvin129.budgetmanager.models.budget_logic;

import fr.vinvin129.budgetmanager.Spent;

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

    public void setAllocationPerMonth(int allocationPerMonth) {
        this.allocationPerMonth = allocationPerMonth;
        //TODO !!!!!
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

    public void addSpent(Spent spent) {
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
