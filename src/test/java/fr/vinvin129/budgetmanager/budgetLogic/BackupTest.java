package fr.vinvin129.budgetmanager.budgetLogic;

import fr.vinvin129.budgetmanager.budgetLogic.moments.BudgetMoment;
import fr.vinvin129.budgetmanager.budgetLogic.moments.CategoryMoment;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class BackupTest {

    @Test
    void save() {
        Backup backup = Backup.INSTANCE;
        Map<Period, BudgetMoment> history = new HashMap<>();
        history.put(new Period(0, 2022), new BudgetMoment(
                "budget test",
                1000,
                700,
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
                                        300,
                                        new CategoryMoment[]{
                                                CategoryMoment.create("cat3", 100)
                                        })
                        )
                }
        ));
        history.put(new Period(1, 2022), new BudgetMoment(
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
        ));

        assertDoesNotThrow(() -> backup.save(history));
        assertEquals(history, backup.load().orElse(null));
    }

    @Test
    void load() {
    }
}