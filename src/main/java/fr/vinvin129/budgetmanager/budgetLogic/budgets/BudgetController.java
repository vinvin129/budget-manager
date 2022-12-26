package fr.vinvin129.budgetmanager.budgetLogic.budgets;

import fr.vinvin129.budgetmanager.budgetLogic.categories.BudgetCategory;
import fr.vinvin129.budgetmanager.budgetLogic.categories.Category;
import fr.vinvin129.budgetmanager.budgetLogic.categories.CategoryController;
import fr.vinvin129.budgetmanager.budgetLogic.moments.BudgetMoment;
import fr.vinvin129.budgetmanager.budgetLogic.moments.CategoryMoment;
import fr.vinvin129.budgetmanager.events.EventT;
import fr.vinvin129.budgetmanager.events.Listener;
import fr.vinvin129.budgetmanager.events.Observable;
import fr.vinvin129.budgetmanager.events.Observer;
import fr.vinvin129.budgetmanager.exceptions.BudgetTooSmallException;
import fr.vinvin129.budgetmanager.exceptions.IllegalBudgetSizeException;
import fr.vinvin129.budgetmanager.exceptions.IllegalCategorySizeException;

import java.util.Arrays;

/**
 * the controller for a {@link Budget} instance
 * @author vinvin129
 */
public class BudgetController extends Observable {
    /**
     * the {@link Listener} for events in {@link CategoryController} of {@link BudgetCategory} linked to this controller
     */
    private final Listener listener = new Listener(EventT.DATA_CHANGE, this::fire);
    /**
     * the {@link Observer} of {@link CategoryController} in this controller
     */
    private final Observer categoryObserver = new Observer() {
        @Override
        protected void onEvent(EventT eventT) {
            fire(eventT);
        }
    };
    /**
     * the {@link Budget} linked to this controller
     */
    private Budget model;

    /**
     * create a new {@link BudgetController} for a {@link Budget} from a {@link BudgetMoment}
     * @param budgetMoment the {@link BudgetMoment} object
     * @throws IllegalBudgetSizeException if illogic size
     */
    public BudgetController(BudgetMoment budgetMoment) throws IllegalBudgetSizeException {
        this.model = Budget.createModel(budgetMoment, this);
        Arrays.stream(this.model.getCategoryControllers()).forEach(this::addListenerOnCategory);
    }

    /**
     * Add listener on a category
     * @param categoryController the {@link CategoryController} object
     */
    private void addListenerOnCategory(CategoryController categoryController) {
        if (categoryController.getModel() instanceof BudgetCategory budgetCategory) {
            budgetCategory.getBudgetController().addListener(listener);
        }
        this.categoryObserver.addObservable(categoryController);
    }

    /**
     * Remove listener on a category
     * @param categoryController the {@link CategoryController} object
     */
    private void removeListenerOnCategory(CategoryController categoryController) {
        if (categoryController.getModel() instanceof BudgetCategory budgetCategory) {
            budgetCategory.getBudgetController().removeListener(listener);
        }
        this.categoryObserver.removeObservable(categoryController);
    }

    /**
     * get this model {@link Budget} for this budget
     * @return a {@link Budget} object
     */
    public Budget getModel() {
        return model;
    }

    /**
     * change the model of this controller
     * @param model the budget model
     */
    public void setModel(Budget model) {
        if (this.model != null) {
            Arrays.stream(this.model.getCategoryControllers()).forEach(this::removeListenerOnCategory);
        }
        this.model = model;
        Arrays.stream(this.model.getCategoryControllers()).forEach(this::addListenerOnCategory);
    }

    /**
     * change the allocation per month for this Budget
     * @param allocationPerMonth money added in the balance each month
     * @throws BudgetTooSmallException thrown if allocationPerMonth is too big for the actual allocation per month of attached {@link Budget}
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

    /**
     * update the balance of budget
     * @param balance the new balance
     */
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

    /**
     * update the name of budget
     * @param name the new name
     */
    public void setName(String name) {
        this.model.setName(name);
    }

    /**
     * add a {@link CategoryController} to this Budget
     * @param categoryMoment the {@link CategoryMoment} object
     * @throws BudgetTooSmallException when there is no more place in this budget
     */
    public CategoryController addCategory(CategoryMoment categoryMoment) throws BudgetTooSmallException, IllegalBudgetSizeException, IllegalCategorySizeException {
        CategoryController categoryController = new CategoryController(categoryMoment, this);
        Category category = categoryController.getModel();
        if ((this.model.getFreeAllocationPerMonth() - category.getAllocationPerMonth()) < 0) {
            throw new BudgetTooSmallException();
        }
        this.model.addCategoryController(categoryController);
        addListenerOnCategory(categoryController);
        fire(EventT.DATA_CHANGE);
        return categoryController;
    }

    /**
     * remove a {@link CategoryController} to this Budget
     * @param categoryController the {@link CategoryController} object
     */
    public void removeCategory(CategoryController categoryController) {
        Category category = categoryController.getModel();
        if (category instanceof BudgetCategory) {
            ((BudgetCategory) category).getBudgetController().removeListener(listener);
        }
        this.model.removeCategoryController(categoryController);
        this.categoryObserver.removeObservable(categoryController);
        fire(EventT.DATA_CHANGE);
    }

    /**
     * remove a {@link CategoryController} to this Budget by this moment
     * @param categoryMoment the {@link CategoryMoment} object
     */
    public void removeCategory(CategoryMoment categoryMoment) {
        removeCategory(Arrays.stream(this.getModel()
                .getCategoryControllers())
                .filter(controller -> controller.getModel().getMoment().equals(categoryMoment))
                .findFirst()
                .orElseThrow());
    }
}
