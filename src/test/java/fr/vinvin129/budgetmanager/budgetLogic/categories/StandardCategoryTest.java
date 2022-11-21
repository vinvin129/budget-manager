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

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class StandardCategoryTest {

    @Test
    void getAllocationPerMonth() throws IllegalCategorySizeException, IllegalBudgetSizeException, BudgetTooSmallException {
        BudgetController budget = new BudgetController(BudgetMoment.create("budget", 1000));
        CategoryController category = budget.addCategory(CategoryMoment.create("category", 300));
        assertEquals(300, category.getModel().getAllocationPerMonth());
    }

    @Test
    void getBalance() throws IllegalCategorySizeException, IllegalBudgetSizeException, BudgetTooSmallException {
        BudgetController budget = new BudgetController(BudgetMoment.create("budget", 1000));
        CategoryController category = budget.addCategory(CategoryMoment.create("category", 300));
        assertEquals(0, category.getModel().getBalance());
        budget.newMonth();
        assertEquals(300, category.getModel().getBalance());
    }

    @Test
    void setAllocationPerMonth() throws IllegalCategorySizeException, IllegalBudgetSizeException, BudgetTooSmallException, CategoryTooBigException {
        BudgetController budget = new BudgetController(BudgetMoment.create("budget", 1000));
        CategoryController category = budget.addCategory(CategoryMoment.create("category", 300));
        budget.newMonth();
        category.setAllocationPerMonth(100);
        assertEquals(100, category.getModel().getAllocationPerMonth());
        assertEquals(300, category.getModel().getBalance());
    }

    @Test
    void newMonth() throws IllegalCategorySizeException, IllegalBudgetSizeException, BudgetTooSmallException {
        BudgetController budget = new BudgetController(BudgetMoment.create("budget", 1000));
        CategoryController category = budget.addCategory(CategoryMoment.create("category", 300));
        budget.newMonth();
        budget.newMonth();
        assertEquals(300, category.getModel().getBalance());
    }

    @Test
    void addSpent() throws IllegalCategorySizeException, IllegalBudgetSizeException, BudgetTooSmallException {
        BudgetController budget = new BudgetController(BudgetMoment.create("budget", 1000));
        CategoryController category = budget.addCategory(CategoryMoment.create("category", 300));
        budget.newMonth();
        category.addSpent(new Spent("dépense", 200, null));
        assertEquals(100, category.getModel().getBalance());
        category.addSpent(new Spent("dépense2", 200, null));
        assertEquals(-100, category.getModel().getBalance());
        budget.newMonth();
        assertEquals(300, category.getModel().getBalance());
    }

    @Test
    void getSpentList() throws IllegalCategorySizeException, IllegalBudgetSizeException, BudgetTooSmallException {
        BudgetController budget = new BudgetController(BudgetMoment.create("budget", 1000));
        CategoryController category = budget.addCategory(CategoryMoment.create("category", 300));
        Spent spent1 = new Spent("dépense", 200, null);
        Spent spent2 = new Spent("dépense2", 200, null);
        budget.newMonth();
        category.addSpent(spent1);
        category.addSpent(spent2);
        budget.newMonth();
        assertArrayEquals(new Spent[]{spent1, spent2}, category.getModel().getSpentList());
    }

    @Test
    void getAmountSpent() throws IllegalCategorySizeException, IllegalBudgetSizeException, BudgetTooSmallException {
        BudgetController budget = new BudgetController(BudgetMoment.create("budget", 1000));
        CategoryController category = budget.addCategory(CategoryMoment.create("category", 300));
        Spent spent1 = new Spent("dépense", 200, null);
        Spent spent2 = new Spent("dépense2", 200, null);
        category.addSpent(spent1);
        category.addSpent(spent2);
        assertEquals(400, category.getModel().getAmountSpent());
    }
}