package fr.vinvin129.budgetmanager.exceptions;

public class IllegalCategorySizeException extends Exception {
    public IllegalCategorySizeException() {
        super("L'allocation de la catégorie ne peut pas être inférieure à 1.");
    }
}
