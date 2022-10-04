package fr.vinvin129.budgetmanager;

import fr.vinvin129.budgetmanager.models.budget_logic.Category;

public record Spent(Category category, String label, int price) {
}
