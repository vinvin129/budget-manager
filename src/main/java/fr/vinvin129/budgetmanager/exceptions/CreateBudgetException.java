package fr.vinvin129.budgetmanager.exceptions;

/**
 * a {@link CreateException} class for graphics errors on budgets creations. Can show a graphic message.
 * @author vinvin129
 */
public class CreateBudgetException extends CreateException{
    /**
     * Create a new exception for graphics budgets creations. the title is defined
     * @param message the description
     */
    public CreateBudgetException(String message) {
        super("Impossible de créer le budget", message);
    }
}
