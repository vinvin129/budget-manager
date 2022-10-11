package fr.vinvin129.budgetmanager.models.budget_logic;

import fr.vinvin129.budgetmanager.Spent;
import fr.vinvin129.budgetmanager.exceptions.IllegalCategorySizeException;

import java.util.ArrayList;
import java.util.List;

/**
 * a {@link Category} without particularity
 * @author vinvin129
 */
public class StandardCategory extends Category{
    /**
     * the actual balance of this category
     */
    private int balance = 0;
    /**
     * the list of expenses in this category
     */
    private final List<Spent> spentList = new ArrayList<>();
    /**
     * money added in the balance each month
     */
    private int allocationPerMonth;

    /**
     * create a {@link StandardCategory} with name and allocation per month
     * @param name the category name
     * @param allocationPerMonth money added in the balance each month
     * @throws IllegalCategorySizeException when allocationPerMonth is under than 1
     */
    public StandardCategory(String name, int allocationPerMonth) throws IllegalCategorySizeException {
        super(name);
        if (allocationPerMonth < 1) {
            throw new IllegalCategorySizeException();
        }
        this.allocationPerMonth = allocationPerMonth;
    }

    /**
     * create a {@link StandardCategory} with name and allocation per month
     * @param name the category name
     * @param allocationPerMonth money added in the balance each month
     * @param balance the actual balance of this category
     * @throws IllegalCategorySizeException when allocationPerMonth is under than 1
     */
    public StandardCategory(String name, int allocationPerMonth, int balance) throws IllegalCategorySizeException {
        super(name);
        if (allocationPerMonth < 1) {
            throw new IllegalCategorySizeException();
        }
        this.balance = balance;
        this.allocationPerMonth = allocationPerMonth;
    }

    @Override
    public int getAllocationPerMonth() {
        return this.allocationPerMonth;
    }

    @Override
    public int getBalance() {
        return balance;
    }

    @Override
    void setAllocationPerMonth(int allocationPerMonth) {

        this.allocationPerMonth = allocationPerMonth;
    }

    @Override
    void newMonth() {
        this.balance = this.getAllocationPerMonth();
    }

    @Override
    void addSpent(Spent spent) {
        if (spent.category() == this) {
            spentList.add(spent);
            this.balance -= spent.price();
        }
    }

    @Override
    public Spent[] getSpentList() {
        return spentList.toArray(new Spent[0]);
    }
}
