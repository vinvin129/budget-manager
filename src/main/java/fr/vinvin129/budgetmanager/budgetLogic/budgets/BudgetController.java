package fr.vinvin129.budgetmanager.budgetLogic.budgets;

import fr.vinvin129.budgetmanager.budgetLogic.categories.BudgetCategory;
import fr.vinvin129.budgetmanager.budgetLogic.categories.Category;
import fr.vinvin129.budgetmanager.budgetLogic.categories.CategoryController;
import fr.vinvin129.budgetmanager.budgetLogic.moments.BudgetMoment;
import fr.vinvin129.budgetmanager.events.EventT;
import fr.vinvin129.budgetmanager.events.Listener;
import fr.vinvin129.budgetmanager.events.Observable;
import fr.vinvin129.budgetmanager.events.Observer;
import fr.vinvin129.budgetmanager.exceptions.BudgetTooSmallException;
import fr.vinvin129.budgetmanager.exceptions.IllegalBudgetSizeException;

import java.util.Arrays;

public class BudgetController extends Observable {
    private final Listener listener = new Listener(EventT.DATA_CHANGE, this::fire);
    private final Observer categoryObserver = new Observer() {
        @Override
        protected void onEvent(EventT eventT) {
            fire(eventT);
        }
    };
    private Budget model;

    public BudgetController(BudgetMoment budgetMoment) throws IllegalBudgetSizeException {
        this.model = Budget.createModel(budgetMoment, this);
    }

    public Budget getModel() {
        return model;
    }

    /**
     * change the allocation per month for this Budget
     * @param allocationPerMonth money added in the balance each month
     * @throws BudgetTooSmallException thrown if allocationPerMonth is too big for the actual allocation per month of attached {@link fr.vinvin129.budgetmanager.models.budget_logic.Budget}
     */
    public void setAllocationPerMonth(double allocationPerMonth) throws BudgetTooSmallException {
        double totalBudget = Arrays.stream(this.model.getCategoryControllers())
                .map(CategoryController::getModel)
                .mapToDouble(Category::getAllocationPerMonth)
                .sum();
        if (totalBudget > allocationPerMonth) {
            throw new BudgetTooSmallException();
        }
        this.model.setAllocationPerMonth(allocationPerMonth);
        fire(EventT.DATA_CHANGE);
    }

    public void setBalance(double balance) {
        this.model.setBalance(balance);
    }

    /**
     * increment balance with allocation per month
     */
    public void newMonth() {
        Arrays.stream(this.model.getCategoryControllers())
                .map(CategoryController::getModel)
                .map(Category::getController)
                .forEach(CategoryController::newMonth);
        this.setBalance(this.model.getBalance() + this.model.getAllocationPerMonth());
        double allocationSum = Arrays.stream(this.model.getCategoryControllers())
                .map(CategoryController::getModel)
                .filter(category -> category instanceof BudgetCategory)
                .mapToDouble(Category::getAllocationPerMonth)
                .sum();
        this.setBalance(this.model.getBalance() - allocationSum);
        fire(EventT.DATA_CHANGE);
    }

    public void setName(String name) {
        this.model.setName(name);
    }

    /**
     * add a {@link CategoryController} to this Budget
     * @param categoryController the {@link CategoryController} object
     * @throws BudgetTooSmallException when there is no more place in this budget
     */
    public void addCategoryController(CategoryController categoryController) throws BudgetTooSmallException {
        Category category = categoryController.getModel();
        if ((this.model.getFreeAllocationPerMonth() - category.getAllocationPerMonth()) < 0) {
            throw new BudgetTooSmallException();
        }
        this.model.addCategoryController(categoryController);
        if (category instanceof BudgetCategory) {
            ((BudgetCategory) category).getBudgetController().addListener(listener);
        }
        this.categoryObserver.addObservable(categoryController);
        fire(EventT.DATA_CHANGE);
    }

    /**
     * remove a {@link CategoryController} to this Budget
     *
     * @param categoryController the {@link CategoryController} object
     */
    public void removeCategoryController(CategoryController categoryController) {
        Category category = categoryController.getModel();
        if (category instanceof BudgetCategory) {
            ((BudgetCategory) category).getBudgetController().removeListener(listener);
        }
        this.model.removeCategoryController(categoryController);
        this.categoryObserver.removeObservable(categoryController);
        fire(EventT.DATA_CHANGE);
    }
}
