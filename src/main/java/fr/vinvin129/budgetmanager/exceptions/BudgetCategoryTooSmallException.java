package fr.vinvin129.budgetmanager.exceptions;

/**
 * Exception for then a {@link fr.vinvin129.budgetmanager.budgetLogic.categories.BudgetCategory} is too small
 * @author vinvin129
 */
public class BudgetCategoryTooSmallException extends Exception {
    public BudgetCategoryTooSmallException() {
        super("Cette catégorie est un budget et la nouvelle allocation est trop petite pour compenser les allocations de ses catégories.\n" +
                "Augmentez l'allocation de cette catégorie/budget ou réduisez les allocations de ses catégories");
    }
}
