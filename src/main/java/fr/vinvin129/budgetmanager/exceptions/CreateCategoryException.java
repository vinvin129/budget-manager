package fr.vinvin129.budgetmanager.exceptions;

public class CreateCategoryException extends CreateException{
    public CreateCategoryException(String message) {
        super("impossible de créer la catégorie", message);
    }
}
