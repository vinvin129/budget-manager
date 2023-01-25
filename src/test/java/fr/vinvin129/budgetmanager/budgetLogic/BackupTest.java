package fr.vinvin129.budgetmanager.budgetLogic;

import fr.vinvin129.budgetmanager.budgetLogic.moments.BudgetMoment;
import fr.vinvin129.budgetmanager.budgetLogic.moments.CategoryMoment;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class BackupTest {
    static File origineFile = new File("backup.json");
    static File origineSaved = new File("backup_temp.json");

    @BeforeAll
    static void beforeAll() throws IOException {
        if (origineFile.exists()) {
            BufferedReader reader = new BufferedReader(new FileReader(origineFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(origineSaved));
            writer.write(reader.readLine());
            writer.close();
            reader.close();
            assertTrue(origineFile.delete());
        }
    }

    @AfterAll
    static void afterAll() throws IOException {
        if (origineSaved.exists()) {
            BufferedReader reader = new BufferedReader(new FileReader(origineSaved));
            BufferedWriter writer = new BufferedWriter(new FileWriter(origineFile));
            writer.write(reader.readLine());
            writer.close();
            reader.close();
            assertTrue(origineSaved.delete());
        }
    }

    @AfterEach
    void afterEach() {
        if (origineFile.exists()) {
            assertTrue(origineFile.delete());
        }
    }

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