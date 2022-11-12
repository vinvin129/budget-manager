package fr.vinvin129.budgetmanager.budgetLogic.budgets;

import fr.vinvin129.budgetmanager.budgetLogic.Spent;
import fr.vinvin129.budgetmanager.budgetLogic.categories.CategoryController;
import fr.vinvin129.budgetmanager.budgetLogic.moments.BudgetMoment;
import fr.vinvin129.budgetmanager.budgetLogic.moments.CategoryMoment;
import fr.vinvin129.budgetmanager.exceptions.BudgetTooSmallException;
import fr.vinvin129.budgetmanager.exceptions.IllegalBudgetSizeException;
import fr.vinvin129.budgetmanager.exceptions.IllegalCategorySizeException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BudgetControllerTest {

    @Test
    void setAllocationPerMonth() throws BudgetTooSmallException, IllegalBudgetSizeException, IllegalCategorySizeException {
        BudgetController b1 = new BudgetController(BudgetMoment.create("budget1", 3000));
        BudgetController b2 = new BudgetController(BudgetMoment.create("budget2", 3000));
        assertThrows(BudgetTooSmallException.class, () -> b1.setAllocationPerMonth(-100));
        b2.addCategory(CategoryMoment.create("cat1b2", 300));
        assertDoesNotThrow(() -> b1.setAllocationPerMonth(1000));
        assertEquals(1000, b1.getModel().getAllocationPerMonth());
        assertDoesNotThrow(() -> b2.setAllocationPerMonth(1000));
        assertEquals(1000, b2.getModel().getAllocationPerMonth());

        b2.setAllocationPerMonth(3000);
        CategoryController cat2b2 = b2.addCategory(CategoryMoment.create("cat2b2", 2000));
        assertThrows(BudgetTooSmallException.class, () -> b2.setAllocationPerMonth(1000));
        b2.removeCategory(cat2b2);
        b2.addCategory(CategoryMoment.create(BudgetMoment.create("cat3b2", 2000)));
        assertThrows(BudgetTooSmallException.class, () -> b2.setAllocationPerMonth(1000));

        assertThrows(BudgetTooSmallException.class, () -> b1.setAllocationPerMonth(-100));
    }

    @Test
    void setBalance() throws IllegalBudgetSizeException {
        BudgetController b1 = new BudgetController(BudgetMoment.create("budget1", 3000));
        b1.setBalance(1000);
        assertEquals(1000, b1.getModel().getBalance());
        b1.setBalance(-345674); // why not ?
        assertEquals(-345674, b1.getModel().getBalance()); // tkt it's ok ..
    }

    @Test
    void newMonth() throws BudgetTooSmallException, IllegalCategorySizeException, IllegalBudgetSizeException {
        BudgetController b = new BudgetController(BudgetMoment.create("budget", 1000));
        CategoryController cat1 = b.addCategory(CategoryMoment.create("cat1", 300));
        assertEquals(0, b.getModel().getBalance());

        b.newMonth();
        assertEquals(1000, b.getModel().getBalance());

        assertDoesNotThrow(() -> cat1.addSpent(new Spent("un achat", 100, null)));
        b.newMonth();
        assertEquals(1900, b.getModel().getBalance());
    }

    @Test
    void setName() throws IllegalBudgetSizeException {
        BudgetController b = new BudgetController(BudgetMoment.create("budget", 1000));
        b.setName("toto");
        assertEquals("toto", b.getModel().getName());
    }

    @Test
    void addCategory() throws IllegalBudgetSizeException, BudgetTooSmallException, IllegalCategorySizeException {
        BudgetController b = new BudgetController(BudgetMoment.create("budget", 1000));
        assertArrayEquals(new CategoryController[]{}, b.getModel().getCategoryControllers());
        CategoryController cat1 = b.addCategory(CategoryMoment.create("cat1", 300));
        CategoryController cat2 = b.addCategory(CategoryMoment.create(BudgetMoment.create("cat2", 300)));
        assertArrayEquals(new CategoryController[]{cat1, cat2}, b.getModel().getCategoryControllers());
        assertThrows(BudgetTooSmallException.class, () -> b.addCategory(CategoryMoment.create("cat3", 600)));
    }

    @Test
    void removeCategory() throws IllegalBudgetSizeException, BudgetTooSmallException, IllegalCategorySizeException {
        BudgetController b = new BudgetController(BudgetMoment.create("budget", 1000));
        CategoryController cat1 = b.addCategory(CategoryMoment.create("cat1", 300));
        CategoryController cat2 = b.addCategory(CategoryMoment.create(BudgetMoment.create("cat2", 300)));
        b.removeCategory(cat1);
        assertArrayEquals(new CategoryController[]{cat2}, b.getModel().getCategoryControllers());
        b.removeCategory(cat2);
        assertArrayEquals(new CategoryController[]{}, b.getModel().getCategoryControllers());
    }
}