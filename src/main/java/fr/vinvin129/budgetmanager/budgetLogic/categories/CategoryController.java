package fr.vinvin129.budgetmanager.budgetLogic.categories;

import fr.vinvin129.budgetmanager.budgetLogic.Spent;
import fr.vinvin129.budgetmanager.budgetLogic.budgets.BudgetController;
import fr.vinvin129.budgetmanager.budgetLogic.moments.CategoryMoment;
import fr.vinvin129.budgetmanager.events.EventT;
import fr.vinvin129.budgetmanager.events.Observable;
import fr.vinvin129.budgetmanager.exceptions.BudgetTooSmallException;
import fr.vinvin129.budgetmanager.exceptions.IllegalBudgetSizeException;
import fr.vinvin129.budgetmanager.exceptions.IllegalCategorySizeException;

/**
 * the controller for a {@link Category} instance
 */
public class CategoryController extends Observable {
    /**
     * the {@link Category} linked to this controller
     */
    private Category model;
    /**
     * this linked {@link BudgetController}
     */
    private final BudgetController budgetParentController;

    /**
     * create a new {@link CategoryController} for a {@link Category} from a {@link CategoryMoment}
     * @param moment this {@link CategoryMoment} moment
     * @param budgetParentController this linked {@link BudgetController}
     * @throws IllegalBudgetSizeException if illogic budget size
     * @throws IllegalCategorySizeException if illogic category size
     */
    public CategoryController(CategoryMoment moment, BudgetController budgetParentController) throws IllegalBudgetSizeException, IllegalCategorySizeException {
        this.model = Category.createModel(moment, this);
        this.budgetParentController = budgetParentController;
    }

    /**
     * get this parent {@link BudgetController}
     * @return a {@link BudgetController} object
     */
    public BudgetController getBudgetParentController() {
        return budgetParentController;
    }

    /**
     * get this model {@link Category} for this category
     * @return a {@link Category} object
     */
    public Category getModel() {
        return model;
    }

    /**
     * update category name
     * @param name new name
     */
    public void setName(String name) {
        this.model.setName(name);
        fire(EventT.DATA_CHANGE);
    }

    /**
     * update category balance
     * @param balance new balance
     */
    public void setBalance(double balance) {
        this.model.setBalance(balance);
        fire(EventT.DATA_CHANGE);
    }

    /**
     * update category allocation per month
     * @param allocationPerMonth new allocation per month
     * @throws BudgetTooSmallException if allocation is too big
     */
    public void setAllocationPerMonth(double allocationPerMonth) throws BudgetTooSmallException {
        this.model.setAllocationPerMonth(allocationPerMonth);
        fire(EventT.DATA_CHANGE);
    }

    /**
     * update balance with allocationPerMonth
     */
    public void newMonth() {
        this.model.newMonth();
        fire(EventT.DATA_CHANGE);
    }

    /**
     * add an expense to expense list of the {@link Category}
     * @param spent the {@link Spent} object
     */
    public void addSpent(Spent spent) {
        this.model.addSpent(spent);
        if (this.model instanceof StandardCategory) {
            this.setBalance(this.model.getBalance() - spent.price());
            this.budgetParentController.setBalance(this.budgetParentController.getModel().getBalance() - spent.price());
        }
        fire(EventT.DATA_CHANGE);
    }
}
