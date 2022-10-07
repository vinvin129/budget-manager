package fr.vinvin129.budgetmanager.models.budget_logic;

import fr.vinvin129.budgetmanager.Spent;
import fr.vinvin129.budgetmanager.exceptions.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Represent a Budget. He can contains some {@link Category}
 * @author vinvin129
 */
public class Budget {
    /**
     * the name of Budget
     */
    private final String name;
    /**
     * money added in the balance each month
     */
    private int allocationPerMonth;
    /**
     * total available in the budget
     */
    private int balance = 0;
    /**
     * {@link Category} List of this Budget
     */
    private final List<Category> categories = new ArrayList<>();

    /**
     * create a budget instance
     * @param name budget name
     * @param allocationPerMonth money added in the balance each month
     */
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

    /**
     * change the allocation per month for this Budget
     * @param allocationPerMonth money added in the balance each month
     * @throws BudgetTooSmallException thrown if allocationPerMonth is too big for the actual allocation per month of attached {@link Budget}
     */
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

    /**
     * change the allocation per month of a {@link Category} in this {@link Budget}
     * @param category the {@link Category} to change allocation
     * @param allocationPerMonth money added in the balance each month
     * @throws BudgetCategoryTooSmallException thrown if allocationPerMonth is too big for the actual allocation per month of attached {@link Budget}
     * @throws CategoryTooBigException thrown if allocationPerMonth is too big for the actual allocation per month of attached {@link Budget}
     * @throws IllegalCategorySizeException thrown when {@link Category} allocation is more small than 1.
     */
    public void setAllocationPerMonthOfCategory(Category category, int allocationPerMonth) throws BudgetCategoryTooSmallException, CategoryTooBigException, IllegalCategorySizeException {
        if (allocationPerMonth <= 1) {
            throw new IllegalCategorySizeException();
        }
        int newTotalAllocation = 0;
        for (Category c : this.categories) {
            newTotalAllocation += c != category ? c.getAllocationPerMonth() : allocationPerMonth;
        }

        if (newTotalAllocation > this.allocationPerMonth) {
            throw new CategoryTooBigException();
        }

        category.setAllocationPerMonth(allocationPerMonth);
    }

    /**
     * increment balance with allocation per month
     */
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

    /**
     * add an expense to expense list of the {@link Category} in {@link Spent} object. This Category need to be in this Budget
     * @param spent the {@link Spent} object
     * @throws BudgetNotContainCategoryException thrown if the {@link Category} specified in {@link Spent} isn't contains here
     */
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
