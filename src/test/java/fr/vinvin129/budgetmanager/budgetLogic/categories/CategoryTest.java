package fr.vinvin129.budgetmanager.budgetLogic.categories;

import fr.vinvin129.budgetmanager.budgetLogic.Spent;
import fr.vinvin129.budgetmanager.budgetLogic.budgets.BudgetController;
import fr.vinvin129.budgetmanager.budgetLogic.moments.BudgetMoment;
import fr.vinvin129.budgetmanager.budgetLogic.moments.CategoryMoment;
import fr.vinvin129.budgetmanager.exceptions.BudgetTooSmallException;
import fr.vinvin129.budgetmanager.exceptions.IllegalBudgetSizeException;
import fr.vinvin129.budgetmanager.exceptions.IllegalCategorySizeException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CategoryTest {

    @Test
    void createModel() throws IllegalBudgetSizeException, IllegalCategorySizeException {
        CategoryMoment moment1 = new CategoryMoment(
                "name",
                300,
                200,
                new Spent[]{
                        new Spent("toto", 20, null)
                },
                null
        );
        CategoryMoment moment2 = new CategoryMoment(
                "name",
                300,
                200,
                new Spent[]{
                        new Spent("toto", 20, null)
                },
                new BudgetMoment(
                        "name",
                        300,
                        200,
                        new CategoryMoment[]{
                                new CategoryMoment(
                                        "name2",
                                        50,
                                        3,
                                        new Spent[]{
                                                new Spent("toto", 20, null)
                                        },
                                        null)})
        );
        Category model1 = Category.createModel(moment1, null);
        Category model2 = Category.createModel(moment2, null);
        Assertions.assertEquals(moment1, model1.getMoment());
        Assertions.assertEquals(moment2, model2.getMoment());

    }

    @Test
    void getName() throws IllegalBudgetSizeException, BudgetTooSmallException, IllegalCategorySizeException {
        BudgetController budgetController = new BudgetController(BudgetMoment.create("toto", 1000));
        CategoryController c = budgetController.addCategory(CategoryMoment.create("theName", 100));
        assertEquals("theName", c.getModel().getName());
    }
}