package fr.vinvin129.budgetmanager.models.budget_logic;

import fr.vinvin129.budgetmanager.Spent;
import fr.vinvin129.budgetmanager.exceptions.IllegalCategorySizeException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StandardCategoryTest {

    @Test
    void createInstance() {
        assertDoesNotThrow(() -> new StandardCategory("category", 300));
        assertThrows(IllegalCategorySizeException.class, () -> new StandardCategory("category", 0));
    }

    @Test
    void getAllocationPerMonth() throws IllegalCategorySizeException {
        StandardCategory category = new StandardCategory("category", 300);
        assertEquals(300, category.getAllocationPerMonth());
    }

    @Test
    void getBalance() throws IllegalCategorySizeException {
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
    void setAllocationPerMonth() throws IllegalCategorySizeException {
        StandardCategory category = new StandardCategory("category", 300);
        category.newMonth();
        category.setAllocationPerMonth(100);
        assertEquals(100, category.getAllocationPerMonth());
        assertEquals(300, category.getBalance());
    }

    @Test
    void newMonth() throws IllegalCategorySizeException {
        StandardCategory category = new StandardCategory("category", 300);
        category.newMonth();
        category.newMonth();
        assertEquals(300, category.getBalance());
    }

    @Test
    void addSpent() throws IllegalCategorySizeException {
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
    void getSpentList() throws IllegalCategorySizeException {
        StandardCategory category = new StandardCategory("category", 300);
        Spent spent1 = new Spent(category, "dépense", 200);
        Spent spent2 = new Spent(category, "dépense2", 200);
        category.newMonth();
        category.addSpent(spent1);
        category.addSpent(spent2);
        category.newMonth();
        assertArrayEquals(new Spent[]{spent1, spent2}, category.getSpentList());
    }

    @Test
    void getAmountSpent() throws IllegalCategorySizeException {
        StandardCategory category = new StandardCategory("category", 300);
        Spent spent1 = new Spent(category, "dépense", 200);
        Spent spent2 = new Spent(category, "dépense2", 200);
        category.addSpent(spent1);
        category.addSpent(spent2);
        assertEquals(400, category.getAmountSpent());
    }
}