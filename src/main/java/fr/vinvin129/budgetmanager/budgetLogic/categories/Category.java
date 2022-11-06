package fr.vinvin129.budgetmanager.budgetLogic.categories;

import fr.vinvin129.budgetmanager.budgetLogic.Spent;
import fr.vinvin129.budgetmanager.budgetLogic.budgets.BudgetController;
import fr.vinvin129.budgetmanager.budgetLogic.moments.CategoryMoment;
import fr.vinvin129.budgetmanager.exceptions.BudgetTooSmallException;
import fr.vinvin129.budgetmanager.exceptions.IllegalBudgetSizeException;
import fr.vinvin129.budgetmanager.exceptions.IllegalCategorySizeException;

import java.util.Arrays;
import java.util.Objects;

public abstract class Category {
    private String name;
    private final CategoryController controller;

    static Category createModel(CategoryMoment moment, CategoryController controller) throws IllegalCategorySizeException, IllegalBudgetSizeException {
        if (moment.budgetMoment() == null) {
            return new StandardCategory(controller, moment.name(), moment.allocationPerMonth());
        } else {
            return new BudgetCategory(controller, new BudgetController(moment.budgetMoment()));
        }
    }

    protected Category(CategoryController controller, String name) {
        this.controller = controller;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    public CategoryController getController() {
        return controller;
    }

    /**
     * get the value of allocation for each month
     * @return the value
     */
    public abstract double getAllocationPerMonth();

    /**
     * get the balance of category
     * @return the balance
     */
    public abstract double getBalance();

    /**
     * update the balance of category
     * @param balance the balance
     */
    abstract void setBalance(double balance);

    /**
     * change the value of allocation for each month
     * @param allocationPerMonth the new allocation
     */
    abstract void setAllocationPerMonth(double allocationPerMonth) throws BudgetTooSmallException;

    /**
     * update balance with allocationPerMonth
     */
    abstract void newMonth();

    /**
     * add a spent in this {@link Category} only if category linked with {@link Spent} it's this. Else do nothing
     * @param spent the {@link Spent} object
     */
    abstract void addSpent(Spent spent) throws IllegalCallerException;

    /**
     * get the list of expenses made in this {@link Category}
     * @return list of expenses
     */
    public abstract Spent[] getSpentList();

    @Override
    public String toString() {
        return this.name + ", allocation : " + getAllocationPerMonth() + ", solde : " + getBalance();
    }

    /**
     * get the amount spent for this {@link Category}
     * @return the amount spent
     */
    public abstract double getAmountSpent();

    public abstract CategoryMoment getMoment();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Category category)) return false;
        return Objects.equals(getName(), category.getName())
                && Objects.equals(getAllocationPerMonth(), category.getAllocationPerMonth())
                && Objects.equals(getBalance(), category.getBalance())
                && Arrays.equals(getSpentList(), category.getSpentList());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }
}
