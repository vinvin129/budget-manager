package fr.vinvin129.budgetmanager.models.budget_logic;

import fr.vinvin129.budgetmanager.Spent;
import fr.vinvin129.budgetmanager.events.EventT;
import fr.vinvin129.budgetmanager.events.Listener;
import fr.vinvin129.budgetmanager.events.Observable;
import fr.vinvin129.budgetmanager.exceptions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Represent a Budget. He can contains some {@link Category}
 * @author vinvin129
 */
public class Budget extends Observable {
    /**
     * the name of Budget
     */
    private final String name;
    /**
     * money added in the balance each month
     */
    private double allocationPerMonth;
    /**
     * total available in the budget
     */
    private double balance = 0;
    /**
     * {@link Category} List of this Budget
     */
    private final List<Category> categories = new ArrayList<>();
    private final Listener listener = new Listener(EventT.DATA_CHANGE, this::fire);

    /**
     * create a budget instance
     * @param name budget name
     * @param allocationPerMonth money added in the balance each month
     * @throws IllegalBudgetSizeException when allocationPerMonth is under than 1
     */
    public Budget(String name, double allocationPerMonth) throws IllegalBudgetSizeException {
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
    public double getAllocationPerMonth() {
        return allocationPerMonth;
    }

    /**
     * get the value of unused allocation of this budget by this categories
     * @return the free allocation per month value
     */
    public double getFreeAllocationPerMonth() {
        double used = 0;
        for (Category category : this.categories) {
            used += category.getAllocationPerMonth();
        }
        return this.allocationPerMonth - used;
    }

    /**
     * get the balance of budget
     * @return the balance
     */
    public double getBalance() {
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
    public void setAllocationPerMonth(double allocationPerMonth) throws BudgetTooSmallException {
        double totalBudget = 0;
        for (Category category : this.categories) {
            totalBudget += category.getAllocationPerMonth();
        }

        if (totalBudget > allocationPerMonth) {
            throw new BudgetTooSmallException();
        }
        this.allocationPerMonth = allocationPerMonth;
        fire(EventT.DATA_CHANGE);
    }

    /**
     * change the allocation per month of a {@link Category} in this {@link Budget}
     * @param category the {@link Category} to change allocation
     * @param allocationPerMonth money added in the balance each month
     * @throws BudgetCategoryTooSmallException thrown if allocationPerMonth is too big for the actual allocation per month of attached {@link Budget}
     * @throws CategoryTooBigException thrown if allocationPerMonth is too big for the actual allocation per month of attached {@link Budget}
     * @throws IllegalCategorySizeException thrown when {@link Category} allocation is more small than 1.
     */
    public void setAllocationPerMonthOfCategory(Category category, double allocationPerMonth) throws BudgetCategoryTooSmallException, CategoryTooBigException, IllegalCategorySizeException {
        if (allocationPerMonth <= 1) {
            throw new IllegalCategorySizeException();
        }
        double newTotalAllocation = 0;
        for (Category c : this.categories) {
            newTotalAllocation += c != category ? c.getAllocationPerMonth() : allocationPerMonth;
        }

        if (newTotalAllocation > this.allocationPerMonth) {
            throw new CategoryTooBigException();
        }

        category.setAllocationPerMonth(allocationPerMonth);
        fire(EventT.DATA_CHANGE);
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
        fire(EventT.DATA_CHANGE);
    }

    /**
     * add a {@link Category} to this Budget
     * @param category the {@link Category} object
     * @throws BudgetTooSmallException when there is no more place in this budget
     */
    public void addCategory(Category category) throws BudgetTooSmallException {
        if ((this.getFreeAllocationPerMonth() - category.getAllocationPerMonth()) < 0) {
            throw new BudgetTooSmallException();
        }
        this.categories.add(category);
        if (category instanceof BudgetCategory) {
            ((BudgetCategory) category).getBudget().addListener(listener);
        }
        fire(EventT.DATA_CHANGE);
    }

    /**
     * remove a {@link Category} to this Budget
     * @param category the {@link Category} object
     */
    public void removeCategory(Category category) {
        if (category instanceof BudgetCategory) {
            ((BudgetCategory) category).getBudget().removeListener(listener);
        }
        this.categories.remove(category);
        fire(EventT.DATA_CHANGE);
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
        fire(EventT.DATA_CHANGE);
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
