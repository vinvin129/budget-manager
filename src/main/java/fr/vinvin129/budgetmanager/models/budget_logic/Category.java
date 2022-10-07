package fr.vinvin129.budgetmanager.models.budget_logic;

import fr.vinvin129.budgetmanager.Spent;
import fr.vinvin129.budgetmanager.exceptions.BudgetCategoryTooSmallException;

/**
 * it's a part of a {@link Budget}. Contains expenses named {@link Spent} to split in categories the expenses
 * @author vinvin129
 */
public abstract class Category {
    /**
     * the category name
     */
    private final String name;

    /**
     * create a {@link Category} object with name
     * @param name the name
     */
    public Category(String name) {
        this.name = name;
    }

    /**
     * get the value of allocation for each month
     * @return the value
     */
    public abstract int getAllocationPerMonth();

    /**
     * get the name of category
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * get the balance of category
     * @return the balance
     */
    public abstract int getBalance();

    /**
     * change the value of allocation for each month
     * @param allocationPerMonth the new allocation
     * @throws BudgetCategoryTooSmallException thown if this {@link Category} contains a Budget and their {@link Category} need more allocation that new value
     */
    abstract void setAllocationPerMonth(int allocationPerMonth) throws BudgetCategoryTooSmallException;

    /**
     * update balance with allocationPerMonth
     */
    abstract void newMonth();

    /**
     * add a spent in this {@link Category} only if category linked with {@link Spent} it's this. Else do nothing
     * @param spent the {@link Spent} object
     */
    abstract void addSpent(Spent spent);

    /**
     * get the list of expenses made in this {@link Category}
     * @return list of expenses
     */
    public abstract Spent[] getSpentList();
}
