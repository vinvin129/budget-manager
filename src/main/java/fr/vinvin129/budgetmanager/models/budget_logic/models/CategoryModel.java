package fr.vinvin129.budgetmanager.models.budget_logic.models;

import fr.vinvin129.budgetmanager.Spent;

import java.io.Serializable;

public class CategoryModel implements Serializable {
    private String name;
    private double allocationPerMonth;
    private double balance;
    private Spent[] expenses;
    private BudgetModel budgetModel = null;

    public CategoryModel(String name, double allocationPerMonth, double balance, Spent[] expenses) {
        this.name = name;
        this.allocationPerMonth = allocationPerMonth;
        this.balance = balance;
        this.expenses = expenses;
    }

    public CategoryModel(String name, double allocationPerMonth, double balance, Spent[] expenses, BudgetModel budgetModel) {
        this.name = name;
        this.allocationPerMonth = allocationPerMonth;
        this.balance = balance;
        this.expenses = expenses;
        this.budgetModel = budgetModel;
    }

    public double getAllocationPerMonth() {
        return allocationPerMonth;
    }

    public double getBalance() {
        return balance;
    }

    public String getName() {
        return name;
    }

    public BudgetModel getBudgetModel() {
        return budgetModel;
    }

    public Spent[] getExpenses() {
        return expenses;
    }

    public void setAllocationPerMonth(double allocationPerMonth) {
        this.allocationPerMonth = allocationPerMonth;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBudgetModel(BudgetModel budgetModel) {
        this.budgetModel = budgetModel;
    }

    public void setExpenses(Spent[] expenses) {
        this.expenses = expenses;
    }
}
