package fr.vinvin129.budgetmanager.ihm;

import fr.vinvin129.budgetmanager.ihm.views.controllers.dashboard.ViewBudgetController;
import fr.vinvin129.budgetmanager.models.budget_logic.Category;

public class DashbordTest {
    static void clickOnChartCategory(FXRobotCustom robotCustom, Category category) {
        robotCustom.clickOnChartDataNode("#budgetGraph", d ->
                d.getName().equals(ViewBudgetController.getCategoryChartDataName(category))
                        && (d.getPieValue() == category.getAmountSpent()
                        || d.getPieValue() == category.getAllocationPerMonth()));
    }
}
