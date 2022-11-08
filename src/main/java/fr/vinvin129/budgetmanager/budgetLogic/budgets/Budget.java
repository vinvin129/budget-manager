package fr.vinvin129.budgetmanager.budgetLogic.budgets;

import fr.vinvin129.budgetmanager.budgetLogic.categories.Category;
import fr.vinvin129.budgetmanager.budgetLogic.categories.CategoryController;
import fr.vinvin129.budgetmanager.budgetLogic.moments.BudgetMoment;
import fr.vinvin129.budgetmanager.budgetLogic.moments.CategoryMoment;
import fr.vinvin129.budgetmanager.exceptions.IllegalBudgetSizeException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * a temporary model to update a {@link BudgetMoment} immutable model
 * @author vinvin129
 */
public class Budget {
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
    private double balance = 0;
    /**
     * {@link Category} List of this Budget
     */
    private final List<CategoryController> categoryControllers = new ArrayList<>();
    /**
     * this controller
     */
    private final BudgetController controller;

    /**
     * create a new {@link Budget} object from a {@link BudgetMoment} object linked with a {@link BudgetController}
     * @param moment the {@link BudgetMoment} object
     * @param controller this controller
     * @return a {@link Budget} instance
     * @throws IllegalBudgetSizeException if size is illogic
     */
    static Budget createModel(BudgetMoment moment, BudgetController controller) throws IllegalBudgetSizeException {
        return new Budget(controller, moment.name(), moment.allocationPerMonth());
    }

    /**
     * create a budget instance
     * @param controller this controller {@link BudgetController}
     * @param name budget name
     * @param allocationPerMonth money added in the balance each month
     * @throws IllegalBudgetSizeException when allocationPerMonth is under than 1
     */
    protected Budget(BudgetController controller, String name, double allocationPerMonth) throws IllegalBudgetSizeException {
        if (allocationPerMonth < 1) {
            throw new IllegalBudgetSizeException();
        }
        this.name = name;
        this.allocationPerMonth = allocationPerMonth;
        this.controller = controller;
    }

    /**
     * get the value of allocation for each month for Budget
     * @return the value
     */
    public double getAllocationPerMonth() {
        return allocationPerMonth;
    }

    /**
     * change the allocation per month for this Budget
     */
    void setAllocationPerMonth(double allocationPerMonth) {
        this.allocationPerMonth = allocationPerMonth;
    }

    /**
     * get the value of unused allocation of this budget by this categories
     * @return the free allocation per month value
     */
    public double getFreeAllocationPerMonth() {
        return this.allocationPerMonth - this.categoryControllers.stream()
                .map(CategoryController::getModel)
                .mapToDouble(Category::getAllocationPerMonth)
                .sum();
    }

    /**
     * get the balance of budget
     * @return the balance
     */
    public double getBalance() {
        return balance;
    }

    /**
     * set the balance of budget
     * @param balance the balance
     */
    void setBalance(double balance) {
        this.balance = balance;
    }

    /**
     * get the name of budget
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * change the name of budget
     * @param name the name
     */
    void setName(String name) {
        this.name = name;
    }

    /**
     * add a {@link Category} to this budget using this {@link CategoryController}
     * @param categoryController the {@link CategoryController} object
     */
    void addCategoryController(CategoryController categoryController) {
        this.categoryControllers.add(categoryController);
    }

    /**
     * remove a {@link Category} to this Budget using this {@link CategoryController}
     */
    void removeCategoryController(CategoryController categoryController) {
        this.categoryControllers.remove(categoryController);
    }

    /**
     * get {@link CategoryController} list of categories controllers in this budget
     * @return {@link CategoryController} list
     */
    public CategoryController[] getCategoryControllers() {
        return this.categoryControllers.toArray(CategoryController[]::new);
    }

    @Override
    public String toString() {
        return "Budget " + name + "; solde : " + balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Budget budget)) return false;
        return getAllocationPerMonth() == budget.getAllocationPerMonth()
                && getBalance() == budget.getBalance()
                && getName().equals(budget.getName())
                && Arrays.equals(getCategoryControllers(), budget.getCategoryControllers());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getAllocationPerMonth(), getBalance(), Arrays.hashCode(getCategoryControllers()));
    }

    /**
     * get this Moment
     * @return the {@link BudgetMoment} object
     */
    public BudgetMoment getMoment() {
        return new BudgetMoment(
                this.getName(),
                this.getAllocationPerMonth(),
                this.getBalance(),
                this.categoryControllers.stream()
                        .map(CategoryController::getModel)
                        .map(Category::getMoment)
                        .toArray(CategoryMoment[]::new)
        );
    }

    /**
     * get this controller
     * @return the {@link BudgetController} object
     */
    public BudgetController getController() {
        return controller;
    }
}
