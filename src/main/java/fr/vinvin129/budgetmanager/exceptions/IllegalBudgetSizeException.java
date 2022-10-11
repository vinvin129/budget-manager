package fr.vinvin129.budgetmanager.exceptions;

public class IllegalBudgetSizeException extends Exception {
    public IllegalBudgetSizeException() {
        super("L'allocation du budget ne peut pas être inférieure à 1.");
    }
}
