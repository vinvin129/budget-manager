package fr.vinvin129.budgetmanager.exceptions;

/**
 * Exception for when a {@link fr.vinvin129.budgetmanager.budgetLogic.budgets.Budget} allocation is more small than 1.
 * @author vinvin129
*/
public class IllegalBudgetSizeException extends Exception {
    public IllegalBudgetSizeException() {
        super("L'allocation du budget ne peut pas être inférieure à 1.");
    }
}
