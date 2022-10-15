package fr.vinvin129.budgetmanager.models.budget_logic;

import fr.vinvin129.budgetmanager.Spent;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CategoryTest {

    @Test
    void getName() {
        Category c = new Category("theName") {
            @Override
            public int getAllocationPerMonth() {
                return 0;
            }

            @Override
            public int getBalance() {
                return 0;
            }

            @Override
            void setAllocationPerMonth(int allocationPerMonth) {

            }

            @Override
            void newMonth() {

            }

            @Override
            void addSpent(Spent spent) {

            }

            @Override
            public Spent[] getSpentList() {
                return new Spent[0];
            }

            @Override
            public double getAmountSpent() {
                return 0;
            }
        };
        assertEquals("theName", c.getName());
    }
}