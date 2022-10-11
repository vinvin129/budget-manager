package fr.vinvin129.budgetmanager.models.budget_logic;

import fr.vinvin129.budgetmanager.Spent;
import fr.vinvin129.budgetmanager.exceptions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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
     * @throws IllegalBudgetSizeException when allocationPerMonth is under than 1
     */
    public Budget(String name, int allocationPerMonth) throws IllegalBudgetSizeException {
        if (allocationPerMonth < 1) {
            throw new IllegalBudgetSizeException();
        }
        this.name = name;
        this.allocationPerMonth = allocationPerMonth;
    }

    /**
     * get the value of allocation for each month for Budget
     * @return the value
     */
    public int getAllocationPerMonth() {
        return allocationPerMonth;
    }

    /**
     * get the balance of budget
     * @return the balance
     */
    public int getBalance() {
        return balance;
    }

    /**
     * get the name of budget
     * @return the name
     */
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

    /**
     * add a {@link Category} to this Budget
     * @param category the {@link Category} object
     * @throws BudgetTooSmallException when there is no more place in this budget
     */
    public void addCategory(Category category) throws BudgetTooSmallException {
        int totalAllocation = 0;
        for (Category c : this.categories) {
            totalAllocation += c.getAllocationPerMonth();
        }
        if ((totalAllocation + category.getAllocationPerMonth()) > this.allocationPerMonth) {
            throw new BudgetTooSmallException();
        }
        this.categories.add(category);
    }

    /**
     * remove a {@link Category} to this Budget
     * @param category the {@link Category} object
     */
    public void removeCategory(Category category) {
        this.categories.remove(category);
    }

    /**
     * get {@link Category} list of categories in this budget
     * @return {@link Category} list
     */
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

    @Override
    public String toString() {
        return "Budget " + name + "; solde : " + balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Budget budget)) return false;
        return getAllocationPerMonth() == budget.getAllocationPerMonth() && getBalance() == budget.getBalance() && getName().equals(budget.getName()) && Arrays.equals(getCategories(), budget.getCategories());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getAllocationPerMonth(), getBalance(), Arrays.hashCode(getCategories()));
    }
}
