package fr.vinvin129.budgetmanager.models.budget_logic;

import fr.vinvin129.budgetmanager.Spent;

import java.util.ArrayList;
import java.util.List;

public class StandardCategory extends Category{
    private int balance = 0;
    private final List<Spent> spentList = new ArrayList<>();
    private int allocationPerMonth;

    public StandardCategory(String name, int allocationPerMonth) {
        super(name);
        this.allocationPerMonth = allocationPerMonth;
    }

    public StandardCategory(String name, int allocationPerMonth, int balance) {
        super(name);
        this.balance = balance;
        this.allocationPerMonth = allocationPerMonth;
    }

    @Override
    public int getAllocationPerMonth() {
        return this.allocationPerMonth;
    }

    @Override
    public int getBalance() {
        return balance;
    }

    @Override
    void setAllocationPerMonth(int allocationPerMonth) {

        this.allocationPerMonth = allocationPerMonth;
    }

    @Override
    void newMonth() {
        this.balance = this.getAllocationPerMonth();
    }

    @Override
    void addSpent(Spent spent) {
        if (spent.category() == this) {
            spentList.add(spent);
            this.balance -= spent.price();
        }
    }

    @Override
    public Spent[] getSpentList() {
        return spentList.toArray(new Spent[0]);
    }
}
