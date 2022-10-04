package fr.vinvin129.budgetmanager.exceptions;

public class CategoryTooBigException extends Exception {
    public CategoryTooBigException() {
        super("l'allocation donnée au budget de cette catégorie est trop petit.\n" +
                "Augmentez cette du budget ou réajustez celle des autres catégories.");
    }
}
