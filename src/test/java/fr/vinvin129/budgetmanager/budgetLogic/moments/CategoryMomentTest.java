package fr.vinvin129.budgetmanager.budgetLogic.moments;

import fr.vinvin129.budgetmanager.budgetLogic.Spent;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryMomentTest {

    @Test
    void createSimple() {
        CategoryMoment categoryMoment = CategoryMoment.create("name", 300);
        assertEquals("name", categoryMoment.name());
        assertEquals(300, categoryMoment.allocationPerMonth());
        assertArrayEquals(new Spent[]{}, categoryMoment.expenses());
        assertNull(categoryMoment.budgetMoment());
    }

    @Test
    void createBudget() {
        BudgetMoment budgetMoment = BudgetMoment.create("name", 600);
        CategoryMoment categoryMoment = CategoryMoment.create(budgetMoment);
        assertEquals("name", categoryMoment.name());
        assertEquals(600, categoryMoment.allocationPerMonth());
        assertArrayEquals(new Spent[]{}, categoryMoment.expenses());
        assertEquals(budgetMoment, categoryMoment.budgetMoment());
    }

    @Test
    void testEquals() {
        CategoryMoment categoryMoment1 = new CategoryMoment(
                "name",
                300,
                0,
                new Spent[]{
                        new Spent("spent1", 300, null)
                },
                BudgetMoment.create("name", 300)
        );
        CategoryMoment categoryMoment2 = new CategoryMoment(
                "name",
                300,
                0,
                new Spent[]{
                        new Spent("spent1", 300, null)
                },
                BudgetMoment.create("name", 300)
        );
        CategoryMoment categoryMoment3 = new CategoryMoment(
                "nam",
                300,
                20,
                new Spent[]{
                        new Spent("spent1", 300, null)
                },
                BudgetMoment.create("name", 300)
        );
        CategoryMoment categoryMoment4 = new CategoryMoment(
                "name",
                300,
                0,
                new Spent[]{
                        new Spent("spent1", 200, null),
                        new Spent("spent2", 300, null)
                },
                BudgetMoment.create("name", 300)
        );
        CategoryMoment categoryMoment5 = new CategoryMoment(
                "name",
                300,
                0,
                new Spent[]{
                        new Spent("spent1", 300, null)
                },
                null
        );
        assertEquals(categoryMoment1, categoryMoment2);
        assertNotEquals(categoryMoment1, categoryMoment3);
        assertNotEquals(categoryMoment1, categoryMoment4);
        assertNotEquals(categoryMoment1, categoryMoment5);
    }
}