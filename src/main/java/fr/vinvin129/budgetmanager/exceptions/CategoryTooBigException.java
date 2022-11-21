package fr.vinvin129.budgetmanager.exceptions;

/**
 * Exception for when the new allocation for a {@link fr.vinvin129.budgetmanager.budgetLogic.categories.Category} is too big
 * @author vinvin129
 */
public class CategoryTooBigException extends Exception {
    public CategoryTooBigException() {
        super("l'allocation donnée au budget de cette catégorie est trop petit.\n" +
                "Augmentez cette du budget ou réajustez celle des autres catégories.");
    }
}
