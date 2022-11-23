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

class BudgetTest {

    @Test
    void createModel() throws IllegalBudgetSizeException {
        BudgetMoment moment1 = new BudgetMoment(
                "name",
                1000,
                200,
                new CategoryMoment[]{
                        new CategoryMoment(
                                "name",
                                300,
                                200,
                                new Spent[]{
                                        new Spent("toto", 20, null)
                                },
                                null
                        )
                });

        BudgetMoment moment2 = new BudgetMoment(
                "name",
                1000,
                200,
                new CategoryMoment[]{
                        new CategoryMoment(
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
                        )
                });
        Budget model1 = Budget.createModel(moment1, null);
        Budget model2 = Budget.createModel(moment2, null);

        assertEquals(moment1, model1.getMoment());
        assertEquals(moment2, model2.getMoment());
    }

    @Test
    void getAllocationPerMonth() throws IllegalBudgetSizeException {
        Budget b = new BudgetController(BudgetMoment.create("budget", 3000)).getModel();
        assertEquals(3000, b.getAllocationPerMonth());
        b.getController().newMonth();
        assertEquals(3000, b.getAllocationPerMonth());
    }

    @Test
    void getBalance() throws BudgetTooSmallException, IllegalCategorySizeException, IllegalBudgetSizeException {
        BudgetController b = new BudgetController(BudgetMoment.create("budget", 3000));
        assertEquals(0, b.getModel().getBalance(), "budget balance must be 0");
        b.newMonth();
        assertEquals(3000, b.getModel().getBalance(), "budget balance must be 3000");

        b = new BudgetController(BudgetMoment.create("budget", 3000));
        b.addCategory(CategoryMoment.create("null", 300));
        b.addCategory(CategoryMoment.create(BudgetMoment.create("null2", 500)));
        assertEquals(0, b.getModel().getBalance(), "budget balance must be 0");
        b.newMonth();
        assertEquals(3000-500, b.getModel().getBalance(), "budget balance must be 3000");
    }

    @Test
    void getName() throws IllegalBudgetSizeException {
        String name = "budget";
        Budget b = new BudgetController(BudgetMoment.create(name, 3000)).getModel();
        assertEquals(name, b.getName());
    }

    @Test
    void setAllocationPerMonth() throws BudgetTooSmallException, IllegalCategorySizeException, IllegalBudgetSizeException {
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
    void addCategoryController() throws IllegalCategorySizeException, IllegalBudgetSizeException, BudgetTooSmallException {
        BudgetController b = new BudgetController(BudgetMoment.create("budget", 1000));
        assertArrayEquals(new CategoryController[]{}, b.getModel().getCategoryControllers());
        CategoryController cat1 = b.addCategory(CategoryMoment.create("cat1", 300));
        CategoryController cat2 = b.addCategory(CategoryMoment.create(BudgetMoment.create("cat2", 300)));
        assertArrayEquals(new CategoryController[]{cat1, cat2}, b.getModel().getCategoryControllers());
        assertThrows(BudgetTooSmallException.class, () -> b.addCategory(CategoryMoment.create("cat3", 600)));
    }

    @Test
    void removeCategoryController() throws IllegalCategorySizeException, IllegalBudgetSizeException, BudgetTooSmallException {
        BudgetController b = new BudgetController(BudgetMoment.create("budget", 1000));
        CategoryController cat1 = b.addCategory(CategoryMoment.create("cat1", 300));
        CategoryController cat2 = b.addCategory(CategoryMoment.create(BudgetMoment.create("cat2", 300)));
        b.removeCategory(cat1);
        assertArrayEquals(new CategoryController[]{cat2}, b.getModel().getCategoryControllers());
        b.removeCategory(cat2);
        assertArrayEquals(new CategoryController[]{}, b.getModel().getCategoryControllers());
    }

    @Test
    void getCategoryControllers() throws IllegalCategorySizeException, IllegalBudgetSizeException, BudgetTooSmallException {
        BudgetController b = new BudgetController(BudgetMoment.create("budget", 1000));
        assertArrayEquals(new CategoryController[]{}, b.getModel().getCategoryControllers());
        CategoryController cat1 = b.addCategory(CategoryMoment.create("cat1", 300));
        assertArrayEquals(new CategoryController[]{cat1}, b.getModel().getCategoryControllers());
    }

    @Test
    void getFreeAllocationPerMonth() throws IllegalBudgetSizeException, IllegalCategorySizeException, BudgetTooSmallException {
        BudgetController b = new BudgetController(BudgetMoment.create("budget", 1000));
        assertEquals(1000, b.getModel().getFreeAllocationPerMonth());
        b.addCategory(CategoryMoment.create("cat1", 300));
        b.addCategory(CategoryMoment.create(BudgetMoment.create("b2", 200)));
        assertEquals(500, b.getModel().getFreeAllocationPerMonth());
    }

    @Test
    void getMoment() throws IllegalBudgetSizeException {
        BudgetMoment realMoment = BudgetMoment.create("budget", 1000);
        assertEquals(realMoment, new BudgetController(realMoment).getModel().getMoment());
        assertNotEquals(
                new BudgetMoment("budget", 1000, 0,
                        new CategoryMoment[]{CategoryMoment.create(BudgetMoment.create("test faux", 300))}),
                new BudgetController(realMoment).getModel().getMoment()
        );
    }
}