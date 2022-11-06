package fr.vinvin129.budgetmanager.budgetLogic.categories;

import fr.vinvin129.budgetmanager.budgetLogic.Spent;
import fr.vinvin129.budgetmanager.budgetLogic.budgets.Budget;
import fr.vinvin129.budgetmanager.budgetLogic.budgets.BudgetController;
import fr.vinvin129.budgetmanager.budgetLogic.moments.CategoryMoment;
import fr.vinvin129.budgetmanager.exceptions.BudgetTooSmallException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BudgetCategory extends Category {
    /**
     * this linked {@link Budget} object
     */
    private final Budget budget;
    private final BudgetController budgetController;

    protected BudgetCategory(CategoryController controller, BudgetController budgetController) {
        super(controller, budgetController.getModel().getName());
        this.budgetController = budgetController;
        this.budget = budgetController.getModel();
    }

    public BudgetController getBudgetController() {
        return budgetController;
    }

    @Override
    public double getAllocationPerMonth() {
        return this.budget.getAllocationPerMonth();
    }

    @Override
    public double getBalance() {
        return this.budget.getAllocationPerMonth();
    }

    @Override
    void setBalance(double balance) {
        this.budgetController.setBalance(balance);
    }

    @Override
    void setAllocationPerMonth(double allocationPerMonth) throws BudgetTooSmallException {
        try {
            this.budgetController.setAllocationPerMonth(allocationPerMonth);
        } catch (BudgetTooSmallException e) {
            throw new BudgetTooSmallException();
        }
    }

    @Override
    void newMonth() {
        this.budgetController.newMonth();
    }

    @Override
    void addSpent(Spent spent) throws IllegalCallerException {
        throw new IllegalCallerException("Budget category can't add spent.");
    }

    @Override
    public Spent[] getSpentList() {
        List<Spent> spentList = new ArrayList<>();
        Arrays.stream(this.budget.getCategoryControllers())
                .map(CategoryController::getModel)
                .forEach(category -> spentList.addAll(List.of(category.getSpentList())));
        return spentList.toArray(Spent[]::new);
    }

    @Override
    public double getAmountSpent() {
        return Arrays.stream(this.budget.getCategoryControllers())
                .map(CategoryController::getModel)
                .mapToDouble(Category::getAmountSpent)
                .sum();
    }

    @Override
    public CategoryMoment getMoment() {
        return new CategoryMoment(
                this.getName(),
                this.getAllocationPerMonth(),
                this.getBalance(),
                this.getSpentList(),
                this.budget.getMoment()
        );
    }
}
