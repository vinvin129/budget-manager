package fr.vinvin129.budgetmanager.exceptions;

/**
 * a {@link CreateException} class for graphics errors on categories creations. Can show a graphic message.
 * @author vinvin129
 */
public class CreateCategoryException extends CreateException{
    /**
     * Create a new exception for graphics categories creations. the title is defined
     * @param message the description
     */
    public CreateCategoryException(String message) {
        super("impossible de créer la catégorie", message);
    }
}
