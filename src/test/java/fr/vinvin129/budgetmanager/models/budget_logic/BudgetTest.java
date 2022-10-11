package fr.vinvin129.budgetmanager.models.budget_logic;

import fr.vinvin129.budgetmanager.Spent;
import fr.vinvin129.budgetmanager.exceptions.BudgetNotContainCategoryException;
import fr.vinvin129.budgetmanager.exceptions.BudgetTooSmallException;
import fr.vinvin129.budgetmanager.exceptions.CategoryTooBigException;
import fr.vinvin129.budgetmanager.exceptions.IllegalCategorySizeException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BudgetTest {

    @Test
    void getAllocationPerMonth() {
        Budget b = new Budget("budget", 3000);
        assertEquals(3000, b.getAllocationPerMonth());
        b.newMonth();
        assertEquals(3000, b.getAllocationPerMonth());
    }

    @Test
    void getBalance() throws BudgetTooSmallException, IllegalCategorySizeException {
        Budget b = new Budget("budget", 3000);
        assertEquals(0, b.getBalance(), "budget balance must be 0");
        b.newMonth();
        assertEquals(3000, b.getBalance(), "budget balance must be 3000");

        b = new Budget("budget", 3000);
        b.addCategory(new StandardCategory("null", 300));
        b.addCategory(new BudgetCategory(new Budget("null2", 500)));
        assertEquals(0, b.getBalance(), "budget balance must be 0");
        b.newMonth();
        assertEquals(3000-500, b.getBalance(), "budget balance must be 3000");
    }

    @Test
    void getName() {
        String name = "budget";
        Budget b = new Budget(name, 3000);
        assertEquals(name, b.getName());
    }

    @Test
    void setAllocationPerMonth() throws BudgetTooSmallException, IllegalCategorySizeException {
        Budget b1 = new Budget("budget1", 3000);
        Budget b2 = new Budget("budget2", 3000);
        Category cat1b2 = new StandardCategory("cat1b2", 300);
        Category cat2b2 = new StandardCategory("cat2b2", 2000);
        Category cat3b2 = new BudgetCategory(new Budget("cat3b2", 2000));
        assertThrows(BudgetTooSmallException.class, () -> b1.setAllocationPerMonth(-100));
        b2.addCategory(cat1b2);
        assertDoesNotThrow(() -> b1.setAllocationPerMonth(1000));
        assertEquals(1000, b1.getAllocationPerMonth());
        assertDoesNotThrow(() -> b2.setAllocationPerMonth(1000));
        assertEquals(1000, b2.getAllocationPerMonth());

        b2.setAllocationPerMonth(3000);
        b2.addCategory(cat2b2);
        assertThrows(BudgetTooSmallException.class, () -> b2.setAllocationPerMonth(1000));
        b2.removeCategory(cat2b2);
        b2.addCategory(cat3b2);
        assertThrows(BudgetTooSmallException.class, () -> b2.setAllocationPerMonth(1000));

        assertThrows(BudgetTooSmallException.class, () -> b1.setAllocationPerMonth(-100));
    }

    @Test
    void setAllocationPerMonthOfCategory() throws BudgetTooSmallException, IllegalCategorySizeException {
        Budget b = new Budget("budget", 1000);
        Category cat1 = new StandardCategory("cat1", 300);
        Category cat2 = new BudgetCategory(new Budget("cat2", 300));
        b.addCategory(cat1);
        b.addCategory(cat2);

        assertDoesNotThrow(() -> b.setAllocationPerMonthOfCategory(cat1, 600));
        assertThrows(CategoryTooBigException.class, () -> b.setAllocationPerMonthOfCategory(cat2, 600));
        assertDoesNotThrow(() -> b.setAllocationPerMonthOfCategory(cat2, 100));

        assertThrows(IllegalCategorySizeException.class, () -> b.setAllocationPerMonthOfCategory(cat1, 0));
        assertThrows(IllegalCategorySizeException.class, () -> b.setAllocationPerMonthOfCategory(cat2, 0));
    }

    @Test
    void newMonth() throws BudgetTooSmallException, IllegalCategorySizeException {
        Budget b = new Budget("budget", 1000);
        Category cat1 = new StandardCategory("cat1", 300);
        b.addCategory(cat1);
        assertEquals(0, b.getBalance());

        b.newMonth();
        assertEquals(1000, b.getBalance());

        assertDoesNotThrow(() -> b.addSpent(new Spent(cat1, "un achat", 100)));
        b.newMonth();
        assertEquals(1900, b.getBalance());
    }

    @Test
    void addCategory() throws IllegalCategorySizeException {
        Budget b = new Budget("budget", 1000);
        Category cat1 = new StandardCategory("cat1", 300);
        Category cat2 = new BudgetCategory(new Budget("cat2", 300));
        Category cat3 = new StandardCategory("cat3", 600);
        assertArrayEquals(new Category[]{}, b.getCategories());
        assertDoesNotThrow(() -> b.addCategory(cat1));
        assertDoesNotThrow(() -> b.addCategory(cat2));
        assertArrayEquals(new Category[]{cat1, cat2}, b.getCategories());
        assertThrows(BudgetTooSmallException.class, () -> b.addCategory(cat3));
    }

    @Test
    void removeCategory() throws IllegalCategorySizeException {
        Budget b = new Budget("budget", 1000);
        Category cat1 = new StandardCategory("cat1", 300);
        Category cat2 = new BudgetCategory(new Budget("cat2", 300));
        assertDoesNotThrow(() -> b.addCategory(cat1));
        assertDoesNotThrow(() -> b.addCategory(cat2));
        b.removeCategory(cat1);
        assertArrayEquals(new Category[]{cat2}, b.getCategories());
        b.removeCategory(cat2);
        assertArrayEquals(new Category[]{}, b.getCategories());
    }

    @Test
    void getCategories() throws IllegalCategorySizeException {
        Budget b = new Budget("budget", 1000);
        Category cat1 = new StandardCategory("cat1", 300);
        assertArrayEquals(new Category[]{}, b.getCategories());
        assertDoesNotThrow(() -> b.addCategory(cat1));
        assertArrayEquals(new Category[]{cat1}, b.getCategories());
    }

    @Test
    void addSpent() throws IllegalCategorySizeException {
        Budget b = new Budget("budget", 1000);
        Category cat1 = new StandardCategory("cat1", 200);
        BudgetCategory cat2 = new BudgetCategory(new Budget("cat2", 300));
        Category cat3 = new StandardCategory("cat3", 300);
        assertDoesNotThrow(() -> b.addCategory(cat1));
        assertDoesNotThrow(() -> b.addCategory(cat2));
        b.newMonth();
        assertDoesNotThrow(() -> b.addSpent(new Spent(cat1, "dep1", 50)));
        assertEquals(1000-300-50, b.getBalance());

        assertThrows(BudgetNotContainCategoryException.class, () -> b.addSpent(new Spent(cat3, "erreur", 20)));
    }
}