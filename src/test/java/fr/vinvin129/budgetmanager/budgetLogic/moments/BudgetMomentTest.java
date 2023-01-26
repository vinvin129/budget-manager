package fr.vinvin129.budgetmanager.budgetLogic.moments;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BudgetMomentTest {

    @Test
    void create() {
        BudgetMoment budgetMoment = BudgetMoment.create("name", 2000);
        assertEquals("name", budgetMoment.name());
        assertEquals(2000, budgetMoment.allocationPerMonth());
        assertEquals(0, budgetMoment.balance());
        assertArrayEquals(new CategoryMoment[]{}, budgetMoment.categoryMoments());
    }

    @Test
    void testEquals() {
        BudgetMoment budgetMoment1 = new BudgetMoment(
                "toto",
                400,
                0,
                new CategoryMoment[]{CategoryMoment.create("test", 200)});
        BudgetMoment budgetMoment2 = new BudgetMoment(
                "toto",
                400,
                0,
                new CategoryMoment[]{CategoryMoment.create("test", 200)});
        BudgetMoment budgetMoment3 = new BudgetMoment(
                "toto",
                400,
                0,
                new CategoryMoment[]{CategoryMoment.create("test", 100)});
        BudgetMoment budgetMoment4 = new BudgetMoment(
                "toto",
                100,
                0,
                new CategoryMoment[]{CategoryMoment.create("test", 200)});
        BudgetMoment budgetMoment5 = new BudgetMoment(
                "toti",
                400,
                200,
                new CategoryMoment[]{CategoryMoment.create("test", 200)});
        assertEquals(budgetMoment1, budgetMoment2);
        assertNotEquals(budgetMoment1, budgetMoment3);
        assertNotEquals(budgetMoment1, budgetMoment4);
        assertNotEquals(budgetMoment1, budgetMoment5);
    }
}