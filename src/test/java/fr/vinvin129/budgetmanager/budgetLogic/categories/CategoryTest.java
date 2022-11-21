package fr.vinvin129.budgetmanager.budgetLogic.categories;

import fr.vinvin129.budgetmanager.budgetLogic.budgets.BudgetController;
import fr.vinvin129.budgetmanager.budgetLogic.moments.BudgetMoment;
import fr.vinvin129.budgetmanager.budgetLogic.moments.CategoryMoment;
import fr.vinvin129.budgetmanager.exceptions.BudgetTooSmallException;
import fr.vinvin129.budgetmanager.exceptions.IllegalBudgetSizeException;
import fr.vinvin129.budgetmanager.exceptions.IllegalCategorySizeException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CategoryTest {

    @Test
    void getName() throws IllegalBudgetSizeException, BudgetTooSmallException, IllegalCategorySizeException {
        BudgetController budgetController = new BudgetController(BudgetMoment.create("toto", 1000));
        CategoryController c = budgetController.addCategory(CategoryMoment.create("theName", 100));
        assertEquals("theName", c.getModel().getName());
    }
}