package fr.vinvin129.budgetmanager.exceptions;

/**
 * Exception for when a {@link fr.vinvin129.budgetmanager.budgetLogic.budgets.Budget} is too small for change {@link fr.vinvin129.budgetmanager.budgetLogic.categories.Category} allocation
 * @author vinvin129
 */
public class BudgetTooSmallException extends Exception {
    public BudgetTooSmallException() {
        super("l'allocation pour ce budget est trop petit. Augmentez le ou réduisez l'allocation de ses catégories");
    }
}
