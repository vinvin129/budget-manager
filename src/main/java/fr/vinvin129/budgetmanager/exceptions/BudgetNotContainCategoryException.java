package fr.vinvin129.budgetmanager.exceptions;

import fr.vinvin129.budgetmanager.budgetLogic.categories.Category;

/**
 * Exception for when a {@link fr.vinvin129.budgetmanager.budgetLogic.budgets.Budget} doesn't contain the {@link Category}
 * @author vinvin129
 */
public class BudgetNotContainCategoryException extends Exception {
    public BudgetNotContainCategoryException(Category category) {
        super("La catégorie " + category.getName() + " n'existe pas dans ce budget.");
    }
}
