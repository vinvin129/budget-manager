package fr.vinvin129.budgetmanager.models.budget_logic;

import fr.vinvin129.budgetmanager.Spent;
import fr.vinvin129.budgetmanager.exceptions.BudgetCategoryTooSmallException;
import fr.vinvin129.budgetmanager.exceptions.BudgetTooSmallException;
import fr.vinvin129.budgetmanager.exceptions.IllegalCategorySizeException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BudgetCategoryTest {

    @Test
    void setAllocationPerMonth() throws BudgetTooSmallException, IllegalCategorySizeException {
        Budget budget = new Budget("budget catégorie", 300);
        BudgetCategory category = new BudgetCategory(budget);
        category.newMonth();
        assertDoesNotThrow(() -> category.setAllocationPerMonth(100));
        assertEquals(100, category.getAllocationPerMonth());
        assertEquals(300, category.getBalance());
        assertThrows(BudgetCategoryTooSmallException.class, () -> category.setAllocationPerMonth(-100));
        budget.addCategory(new StandardCategory("cat2", 50));
        assertThrows(BudgetCategoryTooSmallException.class, () -> category.setAllocationPerMonth(30));
        assertDoesNotThrow(() -> category.setAllocationPerMonth(50));
    }

    @Test
    void getBudget() {
        Budget budget = new Budget("budget catégorie", 300);
        BudgetCategory category = new BudgetCategory(budget);
        assertEquals(budget, category.getBudget());
    }

    @Test
    void getAllocationPerMonth() {
        Budget budget = new Budget("budget catégorie", 300);
        BudgetCategory category = new BudgetCategory(budget);
        assertEquals(300, category.getAllocationPerMonth());
    }

    @Test
    void getBalance() {
        Budget budget = new Budget("budget catégorie", 300);
        BudgetCategory category = new BudgetCategory(budget);
        assertEquals(0, category.getBalance());
        category.newMonth();
        assertEquals(300, category.getBalance());
    }

    @Test
    void newMonth() {
        Budget budget = new Budget("budget catégorie", 300);
        BudgetCategory category = new BudgetCategory(budget);
        category.newMonth();
        category.newMonth();
        assertEquals(600, category.getBalance());
    }

    @Test
    void addSpent() throws BudgetTooSmallException, IllegalCategorySizeException {
        Budget budget = new Budget("budget catégorie", 300);
        Category categoryInB = new StandardCategory("categoryInB", 300);
        budget.addCategory(categoryInB);
        BudgetCategory category = new BudgetCategory(budget);
        category.newMonth();
        assertThrows(RuntimeException.class,
                () -> category.addSpent(new Spent(category, "dépense", 200))); // on sait jamais
        category.addSpent(new Spent(categoryInB, "dépense", 200));
        assertEquals(100, category.getBalance());
        category.addSpent(new Spent(categoryInB, "dépense2", 200));
        assertEquals(-100, category.getBalance());
        category.newMonth();
        assertEquals(200, category.getBalance());
    }

    @Test
    void getSpentList() {
        Budget budget = new Budget("budget catégorie", 300);
        BudgetCategory category = new BudgetCategory(budget);
        assertArrayEquals(null, category.getSpentList());
    }
}