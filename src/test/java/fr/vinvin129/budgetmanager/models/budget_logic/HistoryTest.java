package fr.vinvin129.budgetmanager.models.budget_logic;

import fr.vinvin129.budgetmanager.exceptions.BudgetTooSmallException;
import fr.vinvin129.budgetmanager.exceptions.IllegalBudgetSizeException;
import fr.vinvin129.budgetmanager.exceptions.IllegalCategorySizeException;
import org.junit.jupiter.api.Test;

import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.*;

class HistoryTest {

    @Test
    void getPeriodByDate() throws IllegalBudgetSizeException, IllegalCategorySizeException, BudgetTooSmallException {
        Budget b = new Budget("budget test2", 1000);
        Budget b2 = new Budget("bugcat1", 300);
        StandardCategory cat1 = new StandardCategory("cat1", 100);
        BudgetCategory bugcat1 = new BudgetCategory(b2);
        b2.addCategory(cat1);
        b.addCategory(bugcat1);

        Calendar now = Calendar.getInstance();
        Calendar specificDate = Calendar.getInstance();
        specificDate.set(2012, Calendar.FEBRUARY, 1);
        assertEquals(new Period(now.get(Calendar.MONTH), now.get(Calendar.YEAR)), History.getPeriodByDate(now));
        assertEquals(new Period(Calendar.FEBRUARY, 2012), History.getPeriodByDate(specificDate));
    }

    @Test
    void nextMonth() throws IllegalBudgetSizeException, IllegalCategorySizeException, BudgetTooSmallException {
        Budget b = new Budget("budget test2", 1000);
        Budget b2 = new Budget("bugcat1", 300);
        StandardCategory cat1 = new StandardCategory("cat1", 100);
        BudgetCategory bugcat1 = new BudgetCategory(b2);
        b2.addCategory(cat1);
        b.addCategory(bugcat1);
        b.newMonth();
        History history = new History(b, new Period(Calendar.AUGUST, 2022));
        history.nextMonth();
        assertEquals(new Period(Calendar.SEPTEMBER, 2022), history.getActualPeriod());
        history.nextMonth();
        assertEquals(new Period(Calendar.OCTOBER, 2022), history.getActualPeriod());
        assertEquals(2100, b.getBalance());
        assertEquals(900, b2.getBalance());
        history.nextMonth();
        history.nextMonth();
        history.nextMonth();
        assertEquals(new Period(Calendar.JANUARY, 2023), history.getActualPeriod());
    }

    @Test
    void lastMonth() throws IllegalBudgetSizeException, IllegalCategorySizeException, BudgetTooSmallException {
        Budget b = new Budget("budget test2", 1000);
        Budget b2 = new Budget("bugcat1", 300);
        StandardCategory cat1 = new StandardCategory("cat1", 100);
        BudgetCategory bugcat1 = new BudgetCategory(b2);
        b2.addCategory(cat1);
        b.addCategory(bugcat1);
        b.newMonth();
        History history = new History(b, new Period(Calendar.AUGUST, 2022));
        assertFalse(history.lastMonth());
        assertEquals(new Period(Calendar.AUGUST, 2022), history.getActualPeriod());

        history.nextMonth();
        history.nextMonth();
        assertTrue(history.lastMonth());
        assertEquals(new Period(Calendar.SEPTEMBER, 2022), history.getActualPeriod());
    }

    @Test
    void getActualPeriod() throws IllegalBudgetSizeException, IllegalCategorySizeException, BudgetTooSmallException {
        Budget b = new Budget("budget test2", 1000);
        Budget b2 = new Budget("bugcat1", 300);
        StandardCategory cat1 = new StandardCategory("cat1", 100);
        BudgetCategory bugcat1 = new BudgetCategory(b2);
        b2.addCategory(cat1);
        b.addCategory(bugcat1);
        b.newMonth();
        History history = new History(b, new Period(Calendar.AUGUST, 2022));
        assertEquals(new Period(Calendar.AUGUST, 2022), history.getActualPeriod());
    }

    @Test
    void getBudget() throws IllegalBudgetSizeException, IllegalCategorySizeException, BudgetTooSmallException {
        Budget b = new Budget("budget test2", 1000);
        Budget b2 = new Budget("bugcat1", 300);
        StandardCategory cat1 = new StandardCategory("cat1", 100);
        BudgetCategory bugcat1 = new BudgetCategory(b2);
        b2.addCategory(cat1);
        b.addCategory(bugcat1);
        b.newMonth();
        History history = new History(b, new Period(Calendar.AUGUST, 2022));
        assertEquals(b, history.getBudget());
    }
}