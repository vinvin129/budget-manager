package fr.vinvin129.budgetmanager.budgetLogic;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * an expense
 * @param label the label/name of expense
 * @param price the price in euros of expense
 * @param period the period of expense
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonSerialize
public record Spent(String label, double price, Period period) {
}
