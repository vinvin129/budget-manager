package fr.vinvin129.budgetmanager.budgetLogic;

/**
 * an expense
 * @param label the label/name of expense
 * @param price the price in euros of expense
 * @param period the period of expense
 */
public record Spent(String label, double price, Period period) {
}
