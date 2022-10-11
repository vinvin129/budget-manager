package fr.vinvin129.budgetmanager.exceptions;

public class CreateBudgetException extends CreateException{
    public CreateBudgetException(String message) {
        super("impossible de créer le budget", message);
    }

    public CreateBudgetException(Throwable e) {
        super(e);
    }
}
