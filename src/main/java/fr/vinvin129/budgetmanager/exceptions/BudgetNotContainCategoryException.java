package fr.vinvin129.budgetmanager.exceptions;

import fr.vinvin129.budgetmanager.models.budget_logic.Category;

/**
 * Exception for when a {@link fr.vinvin129.budgetmanager.models.budget_logic.Budget} doesn't contain the {@link Category}
 * @author vinvin129
 */
public class BudgetNotContainCategoryException extends Exception {
    public BudgetNotContainCategoryException(Category category) {
        super("La catégorie " + category.getName() + " n'existe pas dans ce budget.");
    }
}
