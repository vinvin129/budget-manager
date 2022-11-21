package fr.vinvin129.budgetmanager.budgetLogic.categories;

import fr.vinvin129.budgetmanager.budgetLogic.Spent;
import fr.vinvin129.budgetmanager.budgetLogic.moments.CategoryMoment;
import fr.vinvin129.budgetmanager.exceptions.IllegalCategorySizeException;

import java.util.ArrayList;
import java.util.List;

/**
 * a temporary model to update a {@link CategoryMoment} immutable model for standard category
 * @author vinvin129
 */
public class StandardCategory extends Category{
    /**
     * the actual balance of this category
     */
    private double balance = 0;
    /**
     * the list of expenses in this category
     */
    private final List<Spent> spentList = new ArrayList<>();
    /**
     * money added in the balance each month
     */
    private double allocationPerMonth;

    /**
     * create a standard category instance
     * @param controller this {@link CategoryController} controller
     * @param name category name
     * @param allocationPerMonth category allocation
     * @throws IllegalCategorySizeException if illogic size (minus than 1)
     */
    protected StandardCategory(CategoryController controller, String name, double allocationPerMonth) throws IllegalCategorySizeException {
        super(controller, name);
        if (allocationPerMonth < 1) {
            throw new IllegalCategorySizeException();
        }
        this.allocationPerMonth = allocationPerMonth;
    }

    @Override
    public double getAllocationPerMonth() {
        return this.allocationPerMonth;
    }

    @Override
    public double getBalance() {
        return this.balance;
    }

    @Override
    void setBalance(double balance) {
        this.balance = balance;
    }

    @Override
    void setAllocationPerMonth(double allocationPerMonth) {
        this.allocationPerMonth = allocationPerMonth;
    }

    @Override
    void newMonth() {
        this.balance = this.getAllocationPerMonth();
    }

    @Override
    void addSpent(Spent spent) {
        this.spentList.add(spent);
    }

    @Override
    public Spent[] getSpentList() {
        return this.spentList.toArray(Spent[]::new);
    }

    @Override
    public String toString() {
        return "Catégorie standard " + super.toString();
    }

    @Override
    public double getAmountSpent() {
        return this.spentList.stream().mapToDouble(Spent::price).sum();
    }

    @Override
    public CategoryMoment getMoment() {
        return new CategoryMoment(this.getName(), this.getAllocationPerMonth(), this.getBalance(), this.getSpentList(), null);
    }
}
