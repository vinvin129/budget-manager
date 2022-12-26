package fr.vinvin129.budgetmanager.budgetLogic.categories;

import fr.vinvin129.budgetmanager.budgetLogic.Spent;
import fr.vinvin129.budgetmanager.budgetLogic.budgets.BudgetController;
import fr.vinvin129.budgetmanager.budgetLogic.moments.BudgetMoment;
import fr.vinvin129.budgetmanager.budgetLogic.moments.CategoryMoment;
import fr.vinvin129.budgetmanager.exceptions.BudgetTooSmallException;
import fr.vinvin129.budgetmanager.exceptions.CategoryTooBigException;
import fr.vinvin129.budgetmanager.exceptions.IllegalBudgetSizeException;
import fr.vinvin129.budgetmanager.exceptions.IllegalCategorySizeException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryControllerTest {

    @Test
    void getBudgetParentController() throws IllegalBudgetSizeException, BudgetTooSmallException, IllegalCategorySizeException {
        BudgetController budgetController = new BudgetController(BudgetMoment.create("budget", 1000));
        CategoryController categoryController = budgetController.addCategory(CategoryMoment.create("cat", 300));
        assertEquals(budgetController, categoryController.getBudgetParentController());
    }

    @Test
    void setName() throws IllegalBudgetSizeException, BudgetTooSmallException, IllegalCategorySizeException {
        BudgetController budgetController = new BudgetController(BudgetMoment.create("budget", 1000));
        CategoryController categoryController = budgetController.addCategory(CategoryMoment.create("cat", 300));
        categoryController.setName("aaaaaaaaahhhhh :)))");
        assertEquals("aaaaaaaaahhhhh :)))", categoryController.getModel().getName());
    }

    @Test
    void setBalance() throws IllegalBudgetSizeException, BudgetTooSmallException, IllegalCategorySizeException {
        BudgetController budgetController = new BudgetController(BudgetMoment.create("budget", 1000));
        CategoryController categoryController = budgetController.addCategory(CategoryMoment.create("cat", 300));
        categoryController.setBalance(200);
        assertEquals(200, categoryController.getModel().getBalance());
    }

    @Test
    void setAllocationPerMonth() throws IllegalBudgetSizeException, BudgetTooSmallException, IllegalCategorySizeException {
        BudgetController b = new BudgetController(BudgetMoment.create("budget", 1000));
        CategoryController cat1 = b.addCategory(CategoryMoment.create("cat1", 300));
        CategoryController cat2 = b.addCategory(CategoryMoment.create(BudgetMoment.create("cat2", 300)));

        assertDoesNotThrow(() -> cat1.setAllocationPerMonth(600));
        assertThrows(CategoryTooBigException.class, () -> cat2.setAllocationPerMonth(600));
        assertDoesNotThrow(() -> cat2.setAllocationPerMonth(100));

        assertThrows(IllegalCategorySizeException.class, () -> cat1.setAllocationPerMonth(0));
        assertThrows(IllegalCategorySizeException.class, () -> cat2.setAllocationPerMonth(0));
    }

    @Test
    void addSpent() throws IllegalCategorySizeException, IllegalBudgetSizeException, BudgetTooSmallException {
        BudgetController b = new BudgetController(BudgetMoment.create("budget", 1000));
        CategoryController cat1 = b.addCategory(CategoryMoment.create("cat1", 200));
        b.addCategory(CategoryMoment.create(BudgetMoment.create("cat2", 300)));
        CategoryController cat3 = new CategoryController(CategoryMoment.create("cat3", 300), null);
        b.newMonth();
        assertDoesNotThrow(() -> cat1.addSpent(new Spent("dep1", 50, null)));
        assertEquals(1000-300-50, b.getModel().getBalance());

        assertThrows(NullPointerException.class, () -> cat3.addSpent(new Spent("erreur", 20, null)));
    }
}