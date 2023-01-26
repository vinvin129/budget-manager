package fr.vinvin129.budgetmanager.exceptions;

/**
 * Exception for when a {@link fr.vinvin129.budgetmanager.budgetLogic.categories.Category} allocation is more small than 1.
 * @author vinvin129
 */
public class IllegalCategorySizeException extends Exception {
    public IllegalCategorySizeException() {
        super("L'allocation de la catégorie ne peut pas être inférieure à 1.");
    }
}
