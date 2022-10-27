package fr.vinvin129.budgetmanager;

import fr.vinvin129.budgetmanager.models.budget_logic.Category;

/**
 * an expense in specific {@link Category}
 * @param category the {@link Category} of expense
 * @param label the label/name of expense
 * @param price the price in euros of expense
 */
public record Spent(Category category, String label, double price) {
}
