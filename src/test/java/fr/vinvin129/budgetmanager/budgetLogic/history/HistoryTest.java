package fr.vinvin129.budgetmanager.budgetLogic.history;

import fr.vinvin129.budgetmanager.budgetLogic.Period;
import fr.vinvin129.budgetmanager.budgetLogic.Spent;
import fr.vinvin129.budgetmanager.budgetLogic.budgets.Budget;
import fr.vinvin129.budgetmanager.budgetLogic.budgets.BudgetController;
import fr.vinvin129.budgetmanager.budgetLogic.categories.BudgetCategory;
import fr.vinvin129.budgetmanager.budgetLogic.categories.CategoryController;
import fr.vinvin129.budgetmanager.budgetLogic.moments.BudgetMoment;
import fr.vinvin129.budgetmanager.budgetLogic.moments.CategoryMoment;
import fr.vinvin129.budgetmanager.exceptions.IllegalBudgetSizeException;
import org.junit.jupiter.api.Test;

import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class HistoryTest {

    @Test
    void initialize() throws IllegalBudgetSizeException {
        History history = History.INSTANCE;
        BudgetController budgetController = new BudgetController(new BudgetMoment(
                "budget test",
                1000,
                0,
                new CategoryMoment[]{CategoryMoment.create("cat1", 300)}
        ));
        history.initialize(budgetController);
        assertEquals(budgetController.getModel().getMoment(), history.getActualModel().getMoment());
    }

    @Test
    void previousMonth() throws IllegalBudgetSizeException {
        History history = History.INSTANCE;
        BudgetMoment moment = new BudgetMoment(
                "budget test",
                1000,
                0,
                new CategoryMoment[]{
                        new CategoryMoment(
                                "cat1",
                                300,
                                300,
                                new Spent[]{new Spent("toto", 30, null)},
                                null),
                        CategoryMoment.create(
                                new BudgetMoment(
                                        "cat2",
                                        300,
                                        0,
                                        new CategoryMoment[]{
                                                CategoryMoment.create("cat3", 100)
                                        })
                        )
                }
        );
        BudgetMoment momentM2 = new BudgetMoment(
                "budget test",
                1000,
                700,
                new CategoryMoment[]{
                        new CategoryMoment(
                                "cat1",
                                300,
                                300,
                                new Spent[]{},
                                null),
                        CategoryMoment.create(
                                new BudgetMoment(
                                        "cat2",
                                        300,
                                        300,
                                        new CategoryMoment[]{
                                                new CategoryMoment(
                                                        "cat3",
                                                        100,
                                                        100,
                                                        new Spent[]{},
                                                        null)
                                        })
                        )
                }
        );
        BudgetController budgetController = new BudgetController(moment);
        history.initialize(budgetController);
        assertNull(history.previousMonth());
        history.newMonth();
        assertEquals(momentM2, history.nextMonth().getMoment());
        assertEquals(momentM2, history.getActualModel().getMoment());
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 1);
        assertEquals(History.createPeriod(calendar), history.getActualPeriod());
        assertEquals(moment, history.previousMonth().getMoment());
        assertEquals(moment, history.getActualModel().getMoment());
        assertEquals(History.createPeriod(Calendar.getInstance()), history.getActualPeriod());
        assertNull(history.previousMonth());
    }

    @Test
    void nextMonth() throws IllegalBudgetSizeException {
        History history = History.INSTANCE;
        BudgetMoment moment = new BudgetMoment(
                "budget test",
                1000,
                0,
                new CategoryMoment[]{
                        new CategoryMoment(
                                "cat1",
                                300,
                                300,
                                new Spent[]{new Spent("toto", 30, null)},
                                null),
                        CategoryMoment.create(
                                new BudgetMoment(
                                        "cat2",
                                        300,
                                        0,
                                        new CategoryMoment[]{
                                                CategoryMoment.create("cat3", 100)
                                        })
                        )
                }
        );
        BudgetMoment momentM2 = new BudgetMoment(
                "budget test",
                1000,
                700,
                new CategoryMoment[]{
                        new CategoryMoment(
                                "cat1",
                                300,
                                300,
                                new Spent[]{},
                                null),
                        CategoryMoment.create(
                                new BudgetMoment(
                                        "cat2",
                                        300,
                                        300,
                                        new CategoryMoment[]{
                                                new CategoryMoment(
                                                        "cat3",
                                                        100,
                                                        100,
                                                        new Spent[]{},
                                                        null)
                                        })
                        )
                }
        );
        BudgetController budgetController = new BudgetController(moment);
        history.initialize(budgetController);
        assertNull(history.nextMonth());
        history.newMonth();
        assertEquals(momentM2, history.nextMonth().getMoment());
        assertEquals(momentM2, history.getActualModel().getMoment());
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 1);
        assertEquals(History.createPeriod(calendar), history.getActualPeriod());
        assertNull(history.nextMonth());
    }

    @Test
    void newMonth() throws IllegalBudgetSizeException {
        History history = History.INSTANCE;
        BudgetMoment moment = new BudgetMoment(
                "budget test",
                1000,
                0,
                new CategoryMoment[]{
                        new CategoryMoment(
                                "cat1",
                                300,
                                300,
                                new Spent[]{new Spent("toto", 30, null)},
                                null),
                        CategoryMoment.create(
                                new BudgetMoment(
                                        "cat2",
                                        300,
                                        0,
                                        new CategoryMoment[]{
                                                CategoryMoment.create("cat3", 100)
                                        })
                        )
                }
        );
        BudgetController budgetController = new BudgetController(moment);
        history.initialize(budgetController);
        history.newMonth(); //m2
        history.newMonth(); //m3
        history.nextMonth(); //m2
        Budget newModel = history.nextMonth(); //m3
        assertEquals(budgetController.getModel().getMoment(), history.getActualModel().getMoment());
        assertEquals(newModel.getMoment(), history.getActualModel().getMoment());
        assertEquals(new BudgetMoment(
                "budget test",
                1000,
                1400,
                new CategoryMoment[]{
                        new CategoryMoment(
                                "cat1",
                                300,
                                300,
                                new Spent[]{},
                                null),
                        CategoryMoment.create(
                                new BudgetMoment(
                                        "cat2",
                                        300,
                                        600,
                                        new CategoryMoment[]{
                                                new CategoryMoment(
                                                        "cat3",
                                                        100,
                                                        100,
                                                        new Spent[]{},
                                                        null
                                                )
                                        }
                                )
                        )
                }
        ), newModel.getMoment());
    }

    @Test
    void createPeriod() {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(Calendar.MONTH, Calendar.APRIL);
        calendar1.set(Calendar.YEAR, 2022);
        Calendar calendar2 = Calendar.getInstance();
        Period period = History.createPeriod(calendar1);
        assertEquals(new Period(Calendar.APRIL, 2022), period);
        assertEquals(
                new Period(calendar2.get(Calendar.MONTH), calendar2.get(Calendar.YEAR)),
                History.createPeriod()
        );
    }

    @Test
    void getActualModel() throws IllegalBudgetSizeException {
        History history = History.INSTANCE;
        BudgetMoment moment = new BudgetMoment(
                "budget test",
                1000,
                0,
                new CategoryMoment[]{
                        new CategoryMoment(
                                "cat1",
                                300,
                                300,
                                new Spent[]{new Spent("toto", 30, null)},
                                null),
                        CategoryMoment.create(
                                new BudgetMoment(
                                        "cat2",
                                        300,
                                        0,
                                        new CategoryMoment[]{
                                                CategoryMoment.create("cat3", 100)
                                        })
                        )
                }
        );
        BudgetController budgetController = new BudgetController(moment);
        history.initialize(budgetController);
        assertEquals(moment, history.getActualModel().getMoment());
    }

    @Test
    void getActualPeriod() throws IllegalBudgetSizeException {
        History history = History.INSTANCE;
        BudgetMoment moment = new BudgetMoment(
                "budget test",
                1000,
                0,
                new CategoryMoment[]{
                        new CategoryMoment(
                                "cat1",
                                300,
                                300,
                                new Spent[]{new Spent("toto", 30, null)},
                                null),
                        CategoryMoment.create(
                                new BudgetMoment(
                                        "cat2",
                                        300,
                                        0,
                                        new CategoryMoment[]{
                                                CategoryMoment.create("cat3", 100)
                                        })
                        )
                }
        );
        BudgetController budgetController = new BudgetController(moment);
        history.initialize(budgetController);
        Calendar calendar = Calendar.getInstance();
        assertEquals(History.createPeriod(calendar), history.getActualPeriod());
        calendar.add(Calendar.MONTH, 3);
        history.newMonth();
        history.newMonth();
        history.newMonth();

        history.nextMonth();
        history.nextMonth();
        history.nextMonth();
        assertEquals(History.createPeriod(calendar), history.getActualPeriod());
    }

    @Test
    void updateFutureFromPresentChange() throws IllegalBudgetSizeException {
        History history = History.INSTANCE;
        BudgetMoment m1MomentInit = new BudgetMoment(
                "budget test",
                1000,
                0,
                new CategoryMoment[]{
                        new CategoryMoment(
                                "cat1",
                                300,
                                300,
                                new Spent[]{new Spent("toto", 30, null)},
                                null),
                        CategoryMoment.create(
                                new BudgetMoment(
                                        "cat2",
                                        300,
                                        0,
                                        new CategoryMoment[]{
                                                CategoryMoment.create("cat3", 100)
                                        })
                        )
                }
        );
        BudgetMoment m2Moment = new BudgetMoment(
                "budget test",
                1000,
                600,
                new CategoryMoment[]{
                        new CategoryMoment(
                                "cat1",
                                300,
                                300,
                                new Spent[]{},
                                null),
                        CategoryMoment.create(
                                new BudgetMoment(
                                        "cat2",
                                        300,
                                        100,
                                        new CategoryMoment[]{
                                                new CategoryMoment(
                                                        "cat3",
                                                        100,
                                                        100,
                                                        new Spent[]{},
                                                        null
                                                )
                                        })
                        )
                }
        );

        BudgetController budgetController = new BudgetController(m1MomentInit);
        history.initialize(budgetController);
        history.newMonth();

        CategoryController cat1 = budgetController.getModel().getCategoryControllers()[0];
        BudgetController bugCat2 = ((BudgetCategory)budgetController.getModel().getCategoryControllers()[1].getModel())
                .getBudgetController();
        CategoryController cat3 = bugCat2.getModel().getCategoryControllers()[0];
        cat1.addSpent(new Spent("dépense 1", 100, null));
        cat3.addSpent(new Spent("dépense 2", 200, null));
        history.updateFutureFromPresent();
        history.nextMonth();
        assertEquals(m2Moment, budgetController.getModel().getMoment());
    }
}