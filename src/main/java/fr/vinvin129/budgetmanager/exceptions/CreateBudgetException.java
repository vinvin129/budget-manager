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
        super("impossible de créer le budget", message);
    }

    /**
     * Create a new exception for graphics budgets creations. the title is defined
     * @param e the throwable
     */
    public CreateBudgetException(Throwable e) {
        super(e);
    }
}
