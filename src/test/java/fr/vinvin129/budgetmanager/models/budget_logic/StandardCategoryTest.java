package fr.vinvin129.budgetmanager.models.budget_logic;

import fr.vinvin129.budgetmanager.Spent;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class StandardCategoryTest {

    @Test
    void getAllocationPerMonth() {
        StandardCategory category = new StandardCategory("category", 300);
        assertEquals(300, category.getAllocationPerMonth());
    }

    @Test
    void getBalance() {
        StandardCategory category = new StandardCategory("category", 300);
        StandardCategory category2 = new StandardCategory("category2", 300, 200);
        assertEquals(0, category.getBalance());
        assertEquals(200, category2.getBalance());
        category.newMonth();
        category2.newMonth();
        assertEquals(300, category.getBalance());
        assertEquals(300, category2.getBalance());
    }

    @Test
    void setAllocationPerMonth() {
        StandardCategory category = new StandardCategory("category", 300);
        category.newMonth();
        category.setAllocationPerMonth(100);
        assertEquals(100, category.getAllocationPerMonth());
        assertEquals(300, category.getBalance());
    }

    @Test
    void newMonth() {
        StandardCategory category = new StandardCategory("category", 300);
        category.newMonth();
        category.newMonth();
        assertEquals(300, category.getBalance());
    }

    @Test
    void addSpent() {
        StandardCategory category = new StandardCategory("category", 300);
        category.newMonth();
        category.addSpent(new Spent(category, "dépense", 200));
        assertEquals(100, category.getBalance());
        category.addSpent(new Spent(category, "dépense2", 200));
        assertEquals(-100, category.getBalance());
        category.newMonth();
        assertEquals(300, category.getBalance());
    }

    @Test
    void getSpentList() {
        StandardCategory category = new StandardCategory("category", 300);
        Spent spent1 = new Spent(category, "dépense", 200);
        Spent spent2 = new Spent(category, "dépense2", 200);
        category.newMonth();
        category.addSpent(spent1);
        category.addSpent(spent2);
        category.newMonth();
        assertArrayEquals(new Spent[]{spent1, spent2}, category.getSpentList());
    }
}