package fr.vinvin129.budgetmanager.budgetLogic.categories;

import fr.vinvin129.budgetmanager.budgetLogic.Spent;
import fr.vinvin129.budgetmanager.budgetLogic.moments.CategoryMoment;
import fr.vinvin129.budgetmanager.events.EventT;
import fr.vinvin129.budgetmanager.events.Observable;
import fr.vinvin129.budgetmanager.exceptions.BudgetTooSmallException;
import fr.vinvin129.budgetmanager.exceptions.IllegalBudgetSizeException;
import fr.vinvin129.budgetmanager.exceptions.IllegalCategorySizeException;

public class CategoryController extends Observable {
    private Category model;

    public CategoryController(CategoryMoment moment) throws IllegalBudgetSizeException, IllegalCategorySizeException {
        this.model = Category.createModel(moment, this);
    }

    public Category getModel() {
        return model;
    }

    public void setName(String name) {
        this.model.setName(name);
        fire(EventT.DATA_CHANGE);
    }

    public void setBalance(double balance) {
        this.model.setBalance(balance);
        fire(EventT.DATA_CHANGE);
    }

    public void setAllocationPerMonth(double allocationPerMonth) throws BudgetTooSmallException {
        this.model.setAllocationPerMonth(allocationPerMonth);
        fire(EventT.DATA_CHANGE);
    }

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
        }
        fire(EventT.DATA_CHANGE);
    }
}
