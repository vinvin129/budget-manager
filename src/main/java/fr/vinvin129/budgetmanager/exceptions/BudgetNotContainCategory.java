package fr.vinvin129.budgetmanager.exceptions;

import fr.vinvin129.budgetmanager.models.budget_logic.Category;

public class BudgetNotContainCategory extends Exception {
    public BudgetNotContainCategory(Category category) {
        super("La catégorie " + category.getName() + " n'existe pas dans ce budget.");
    }
}
