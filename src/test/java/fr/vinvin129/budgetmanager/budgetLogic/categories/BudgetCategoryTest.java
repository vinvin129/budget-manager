package fr.vinvin129.budgetmanager.budgetLogic.categories;

import fr.vinvin129.budgetmanager.budgetLogic.Spent;
import fr.vinvin129.budgetmanager.budgetLogic.budgets.BudgetController;
import fr.vinvin129.budgetmanager.budgetLogic.moments.BudgetMoment;
import fr.vinvin129.budgetmanager.budgetLogic.moments.CategoryMoment;
import fr.vinvin129.budgetmanager.exceptions.BudgetTooSmallException;
import fr.vinvin129.budgetmanager.exceptions.IllegalBudgetSizeException;
import fr.vinvin129.budgetmanager.exceptions.IllegalCategorySizeException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BudgetCategoryTest {

    @Test
    void setAllocationPerMonth() throws BudgetTooSmallException, IllegalCategorySizeException, IllegalBudgetSizeException {
        BudgetController budget = new BudgetController(BudgetMoment.create("budget", 1000));
        CategoryController category = budget.addCategory(CategoryMoment.create(BudgetMoment.create("budget catégorie", 300)));
        budget.newMonth();
        assertDoesNotThrow(() -> category.setAllocationPerMonth(100));
        assertEquals(100, category.getModel().getAllocationPerMonth());
        assertEquals(300, category.getModel().getBalance());
        assertThrows(IllegalCategorySizeException.class, () -> category.setAllocationPerMonth(-100));
        ((BudgetCategory) category.getModel())
                .getBudgetController()
                .addCategory(CategoryMoment.create("cat2", 50));
        assertThrows(BudgetTooSmallException.class, () -> category.setAllocationPerMonth(30));
        assertDoesNotThrow(() -> category.setAllocationPerMonth(50));
    }

    @Test
    void getBudgetController() throws IllegalBudgetSizeException, BudgetTooSmallException, IllegalCategorySizeException {
        BudgetController budget = new BudgetController(BudgetMoment.create("budget", 1000));
        BudgetMoment budgetMoment = BudgetMoment.create("budget catégorie", 300);
        CategoryController category = budget.addCategory(CategoryMoment.create(budgetMoment));
        assertEquals(budgetMoment, ((BudgetCategory) category.getModel()).getBudgetController().getModel().getMoment());
    }

    @Test
    void getAllocationPerMonth() throws IllegalBudgetSizeException, BudgetTooSmallException, IllegalCategorySizeException {
        BudgetController budget = new BudgetController(BudgetMoment.create("budget", 1000));
        CategoryController category = budget.addCategory(CategoryMoment.create(BudgetMoment.create("budget catégorie", 300)));
        assertEquals(300, category.getModel().getAllocationPerMonth());
    }

    @Test
    void getBalance() throws IllegalBudgetSizeException, BudgetTooSmallException, IllegalCategorySizeException {
        BudgetController budget = new BudgetController(BudgetMoment.create("budget", 1000));
        CategoryController category = budget.addCategory(CategoryMoment.create(BudgetMoment.create("budget catégorie", 300)));
        assertEquals(0, category.getModel().getBalance());
        budget.newMonth();
        assertEquals(300, category.getModel().getBalance());
    }

    @Test
    void newMonth() throws IllegalBudgetSizeException, BudgetTooSmallException, IllegalCategorySizeException {
        BudgetController budget = new BudgetController(BudgetMoment.create("budget", 1000));
        CategoryController category = budget.addCategory(CategoryMoment.create(BudgetMoment.create("budget catégorie", 300)));
        budget.newMonth();
        budget.newMonth();
        assertEquals(600, category.getModel().getBalance());
    }

    @Test
    void addSpent() throws BudgetTooSmallException, IllegalCategorySizeException, IllegalBudgetSizeException {
        BudgetController budget = new BudgetController(BudgetMoment.create("budget", 1000));
        CategoryController category = budget.addCategory(CategoryMoment.create(BudgetMoment.create("budget catégorie", 300)));
        CategoryController categoryInB = ((BudgetCategory) category.getModel())
                .getBudgetController()
                .addCategory(CategoryMoment.create("categoryInB", 300));
        budget.newMonth();
        assertThrows(RuntimeException.class,
                () -> category.addSpent(new Spent("dépense", 200, null))); // on sait jamais
        categoryInB.addSpent(new Spent("dépense", 200, null));
        assertEquals(100, category.getModel().getBalance());
        categoryInB.addSpent(new Spent("dépense2", 200, null));
        assertEquals(-100, category.getModel().getBalance());
        budget.newMonth();
        assertEquals(200, category.getModel().getBalance());
    }

    @Test
    void getSpentList() throws IllegalBudgetSizeException, BudgetTooSmallException, IllegalCategorySizeException {
        BudgetController budget = new BudgetController(BudgetMoment.create("budget", 1000));
        CategoryController category = budget.addCategory(CategoryMoment.create(BudgetMoment.create("budget catégorie", 300)));
        assertArrayEquals(new Spent[]{}, category.getModel().getSpentList());
    }

    @Test
    void getAmountSpent() throws IllegalBudgetSizeException, IllegalCategorySizeException, BudgetTooSmallException {
        BudgetController budget = new BudgetController(BudgetMoment.create("budget", 1000));
        CategoryController category = budget.addCategory(CategoryMoment.create(BudgetMoment.create("budget catégorie", 300)));
        CategoryController cat1 = ((BudgetCategory)category.getModel()).getBudgetController().addCategory(CategoryMoment.create("cat1", 300));
        Spent spent1 = new Spent("dépense", 200, null);
        Spent spent2 = new Spent("dépense2", 200, null);
        cat1.addSpent(spent1);
        cat1.addSpent(spent2);
        assertEquals(400, category.getModel().getAmountSpent());
    }
}