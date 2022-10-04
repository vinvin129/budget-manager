package fr.vinvin129.budgetmanager.exceptions;

public class BudgetTooSmallException extends Exception {
    public BudgetTooSmallException() {
        super("l'allocation pour ce budget est trop petit. Augmentez le ou réduisez l'allocation de ses catégories");
    }
}
